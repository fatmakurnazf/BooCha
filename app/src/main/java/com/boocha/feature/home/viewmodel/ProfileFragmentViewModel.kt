package com.boocha.feature.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boocha.data.Repository
import com.boocha.data.remote.FirebaseService
import com.boocha.data.remote.util.Resource
import com.boocha.data.remote.util.Status
import com.boocha.model.Swap
import com.boocha.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.io.File

class ProfileFragmentViewModel : ViewModel() {

    val userLiveData = MutableLiveData<Resource<User?>>()
    val swapsLiveData = MutableLiveData<Resource<MutableList<Swap>>>()
    val updateProfilePhotoLiveData = MutableLiveData<Resource<File>>()

    private val repository = Repository(FirebaseService())

    fun getUser(id: String) {
        userLiveData.value = Resource(Status.LOADING, null, null)

        repository.getUser(id, OnSuccessListener { user ->
            userLiveData.value = Resource(Status.SUCCESS, user, null)

        }, OnFailureListener {
            userLiveData.value = Resource(Status.ERROR, null, null)
        })
    }

    fun getUserSwaps(id: String) {
        repository.getUserSwaps(id, OnSuccessListener {
            swapsLiveData.value = Resource(Status.SUCCESS, it, null)
        }, OnFailureListener {
            swapsLiveData.value = Resource(Status.ERROR, null, it.localizedMessage)
        })
    }

    fun getCurrentUserAccount() {
        repository.getCurrentUserAccount()
    }

    fun signOut() {
        repository.signOut()
    }

    fun updateProfilePhoto(id: String, imageFile: File) {
        updateProfilePhotoLiveData.value = Resource.loading(null)
        repository.updateProfilePhoto(id, imageFile
                , OnSuccessListener {
            updateProfilePhotoLiveData.value = Resource.success(imageFile)

        }, OnFailureListener {
            updateProfilePhotoLiveData.value = Resource.error(it.localizedMessage, null)

        })
    }
}