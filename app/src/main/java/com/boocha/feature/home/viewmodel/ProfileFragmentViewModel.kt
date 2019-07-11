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

class ProfileFragmentViewModel : ViewModel() {

    val userLiveData = MutableLiveData<Resource<User?>>()

    private val repository = Repository(FirebaseService())

    fun getUser(id: String) {
        userLiveData.value = Resource(Status.LOADING, null, null)

        repository.getUser(id, OnSuccessListener { user ->
            userLiveData.value = Resource(Status.SUCCESS, user, null)

        }, OnFailureListener {
            userLiveData.value = Resource(Status.ERROR, null, null)
        })
    }

    fun getCurrentUserAccount() {
        repository.getCurrentUserAccount()
    }
}