package com.boocha.data.remote

import android.util.Log
import androidx.core.net.toUri
import com.boocha.data.remote.util.SWAP_LIST
import com.boocha.data.remote.util.USERS
import com.boocha.model.Swap
import com.boocha.model.User
import com.boocha.util.getCurrentTime
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*


class FirebaseService {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storage = FirebaseStorage.getInstance()
    private var storageRef = storage.reference
    private val writeBatch = database.batch()

    fun login(email: String, password: String, onSuccessListener: OnSuccessListener<AuthResult>, onFailureListener: OnFailureListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { response ->
                    firebaseAuth.currentUser?.also {
                        updateLastLogin(it.uid)
                    }
                    onSuccessListener.onSuccess(response)
                }
                .addOnFailureListener { exception ->
                    onFailureListener.onFailure(exception)
                }
    }

    fun signUp(user: User, onSuccessListener: OnSuccessListener<Void>, onFailureListener: OnFailureListener) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener {
                    user.id = firebaseAuth.currentUser?.uid ?: ""
                    saveUserToDatabase(user, onSuccessListener, onFailureListener)
                }
                .addOnFailureListener { exception ->
                    onFailureListener.onFailure(exception)
                }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getUser(id: String, onSuccessListener: OnSuccessListener<User>, onFailureListener: OnFailureListener) {
        database.collection(USERS).document(id).get()
                .addOnSuccessListener { user ->
                    onSuccessListener.onSuccess(snapshotToUser(user))
                }
                .addOnFailureListener {
                    onFailureListener.onFailure(it)
                }
    }

    fun getCurrentUserAccount(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun updateLastLogin(id: String?) {
        if (id != null) {
            database.collection(USERS).document(id).update("lastLogin", getCurrentTime())
        }
    }

    fun getSwapList(onSuccessListener: OnSuccessListener<MutableList<Swap>>, onFailureListener: OnFailureListener) {
        database.collection(SWAP_LIST).orderBy("date", Query.Direction.DESCENDING).limit(40).get()
                .addOnSuccessListener { swaps ->
                    onSuccessListener.onSuccess(swaps.toObjects(Swap::class.java))
                }
                .addOnFailureListener {
                    onFailureListener.onFailure(it)
                }
    }

    fun searchBook(name: String) {
        database.collection("books").orderBy("name").startAt(name).get()
                .addOnSuccessListener {
                    Log.e("", "")
                }
                .addOnFailureListener {
                    Log.e("", "")
                }
    }

    fun addSwap(image: File, swap: Swap, onSuccessListener: OnSuccessListener<Void>, onFailureListener: OnFailureListener) {
        val id = UUID.randomUUID()

        val referance = storageRef.child("bookPhotos/$id")
        val uploadTask = storageRef.child("bookPhotos/$id").putFile(image.toUri())
                .addOnSuccessListener {
                    referance.downloadUrl.addOnSuccessListener { uri ->
                        swap.imageUri = uri.toString()

                        val documentId = UUID.randomUUID()
                        writeBatch.set(database.collection(SWAP_LIST).document(documentId.toString()), swap)
                        writeBatch.set(database.collection(USERS).document(getCurrentUserAccount()?.uid!!).collection(SWAP_LIST).document(documentId.toString()), mapOf(
                                "data" to swap.date,
                                "swapStatus" to swap.swapStatus,
                                "imageUri" to swap.imageUri,
                                "book" to swap.book
                        ))
                        writeBatch.commit().addOnSuccessListener {
                            onSuccessListener.onSuccess(it)
                        }.addOnFailureListener {
                            onFailureListener.onFailure(it)
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("", "")
                }
    }

    private fun snapshotToUser(snapshot: DocumentSnapshot): User {
        val id = snapshot.id
        val name = snapshot.get("name") as String
        val surname = snapshot.get("surname") as String
        val profilePhoto = snapshot.get("profilePhoto") as String
        val email = snapshot.get("email") as String
        val password = snapshot.get("password") as String
        val lastLogin = snapshot.get("lastLogin") as String

        return User(id, name, surname, profilePhoto, email, password, lastLogin)
    }

    private fun snapshotToBook(snapshot: DocumentSnapshot) {
        val bookId = snapshot.id
        val name = snapshot.get("name") as String
        val author = snapshot.get("author") as String
        val page = snapshot.get("page") as String
        val photo = snapshot.get("photo") as String
        val description = snapshot.get("description") as String

        //return Book(bookId, name, author, photo, page, description)
    }

    private fun saveUserToDatabase(user: User, onSuccessListener: OnSuccessListener<Void>, onFailureListener: OnFailureListener) {
        val data: HashMap<String, String> = hashMapOf(
                "name" to user.name,
                "surname" to user.surname,
                "profilePhoto" to user.profilePhoto,
                "email" to user.email,
                "password" to user.password,
                "lastLogin" to getCurrentTime()
        )

        database.collection(USERS).document(user.id).set(data)
                .addOnSuccessListener {
                    onSuccessListener.onSuccess(it)
                    signOut()
                }
                .addOnFailureListener {
                    onFailureListener.onFailure(it)
                }
    }
}


