package com.example.countryquizz.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countryquizz.data.firebase.FirebaseService
import com.google.firebase.auth.FirebaseUser

class RegistrationViewModel(private val auth: FirebaseService): ViewModel() {

    var currentUser = MutableLiveData<FirebaseUser>()
    var isSignedIn = MutableLiveData<Boolean>()
    var isRegistered = MutableLiveData<Boolean>()

    suspend fun register(email: String, password: String) {
        try {
            auth.register(email, password)
            if(auth.isRegistered())
                isRegistered.postValue(true)
            else
                isRegistered.postValue(false)
        }
        catch (e: Exception) {
            Log.e("RegistrationException", e.stackTraceToString())
        }
    }

    suspend fun login(email: String, password: String) {
        try {
            auth.login(email, password)
            if(auth.isSigned()) {
                currentUser.postValue(auth.getCurrentUser())
                isSignedIn.postValue(isSigned())
            }
            else {
                isSignedIn.postValue(isSigned())
            }
        }
        catch (e: Exception) {
            Log.e("LoginException", e.stackTraceToString())
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun isSigned(): Boolean {
        return auth.isSigned()
    }

    fun getUsername(): String? {
        return auth.getUsername()
    }
}