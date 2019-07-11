package com.boocha.data

import com.boocha.data.remote.FirebaseService
import com.boocha.data.remote.util.RetrofitServiceGenerator
import com.boocha.model.Swap
import com.boocha.model.User
import com.boocha.model.book.SearchResponse
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import java.io.File

class Repository(private val firebaseService: FirebaseService) {

    fun login(
            email: String,
            password: String,
            onSuccessListener: OnSuccessListener<AuthResult>,
            onFailureListener: OnFailureListener
    ) {
        firebaseService.login(email, password, onSuccessListener, onFailureListener)
    }

    fun signUp(user: User, onSuccessListener: OnSuccessListener<Void>, onFailureListener: OnFailureListener) {
        firebaseService.signUp(user, onSuccessListener, onFailureListener)
    }

    fun getUser(id: String, onSuccessListener: OnSuccessListener<User>, onFailureListener: OnFailureListener) {
        firebaseService.getUser(id, onSuccessListener, onFailureListener)
    }

    fun getCurrentUserAccount(): FirebaseUser? {
        return firebaseService.getCurrentUserAccount()
    }

    fun updateLastLogin() {
        return firebaseService.updateLastLogin(getCurrentUserAccount()?.uid)
    }

    fun getSwapList(onSuccessListener: OnSuccessListener<MutableList<Swap>>, onFailureListener: OnFailureListener) {
        firebaseService.getSwapList(onSuccessListener, onFailureListener)
    }

    fun searchBook(query: String): Observable<SearchResponse> {
        return RetrofitServiceGenerator.service().searchBook("intitle:$query")
    }

    fun addSwap(image: File, swap: Swap, onSuccessListener: OnSuccessListener<Void>, onFailureListener: OnFailureListener) {
        firebaseService.addSwap(image, swap, onSuccessListener, onFailureListener)
    }

}