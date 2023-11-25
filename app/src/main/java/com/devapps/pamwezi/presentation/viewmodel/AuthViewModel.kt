package com.devapps.pamwezi.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapps.pamwezi.domain.model.UserData
import com.devapps.pamwezi.domain.repository.FirebaseAuthClientRepository
import com.devapps.pamwezi.domain.repository.FirestoreRepository
import com.devapps.pamwezi.domain.repository.FirestoreRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val firebaseAuthClientRepository: FirebaseAuthClientRepository
    private val firestoreRepository: FirestoreRepository

    init {
        val authClient = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        firebaseAuthClientRepository = FirebaseAuthClientRepository(authClient, FirestoreRepositoryImpl(firestore))
        firestoreRepository = FirestoreRepositoryImpl(firestore)
    }

    private val _registrationResult = MutableLiveData<Result<Unit>>()
    val registrationResult: LiveData<Result<Unit>> get() = _registrationResult

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    suspend fun registerUser(username: String, email: String, password: String)  {

        viewModelScope.launch {
            val result = firebaseAuthClientRepository.registerUser(username, email, password)
            _registrationResult.value = result

            if (result.isSuccess) {
                val user = UserData(username, email)
                firestoreRepository.insertUser(user)
            }
        }
    }

    fun resetUser() {
        _username.value = null
    }

}