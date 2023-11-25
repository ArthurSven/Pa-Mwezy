package com.devapps.pamwezi.domain.repository

import com.devapps.pamwezi.domain.model.UserData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthClientRepository (
    private var authClient: FirebaseAuth,
    private val firestoreRepository: FirestoreRepository){
    suspend fun registerUser(username: String, email: String, password: String) : Result<Unit> {
        return try {
            authClient.createUserWithEmailAndPassword(email, password).await()

            val user = UserData(username, email)
            firestoreRepository.insertUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}