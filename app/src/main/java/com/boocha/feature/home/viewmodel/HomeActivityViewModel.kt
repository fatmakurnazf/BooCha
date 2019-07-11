package com.boocha.feature.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boocha.data.Repository
import com.boocha.data.remote.FirebaseService
import com.boocha.data.remote.util.Resource
import com.boocha.data.remote.util.Status
import com.boocha.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseUser


class HomeActivityViewModel : ViewModel() {

    val userLiveData = MutableLiveData<Resource<User?>>()

    private val repository = Repository(FirebaseService())

    fun getCurrentFirebaseUser(): FirebaseUser? {
        return repository.getCurrentUserAccount()
    }

    fun getCurrentUser() {
        getCurrentFirebaseUser()?.uid?.let { id ->
            repository.getUser(id, OnSuccessListener {
                userLiveData.value = Resource(Status.SUCCESS, it, "")
            }, OnFailureListener {
                userLiveData.value = Resource(Status.ERROR, null, "")
            })
        }
    }

    fun updateLastLogin() {
        repository.updateLastLogin()
    }
}