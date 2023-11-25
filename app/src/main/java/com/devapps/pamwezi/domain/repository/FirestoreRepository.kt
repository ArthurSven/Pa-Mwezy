package com.devapps.pamwezi.domain.repository

import com.devapps.pamwezi.domain.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.tasks.await

interface FirestoreRepository {
    suspend fun insertUser(user: UserData) : Result<Unit>
}

class FirestoreRepositoryImpl(private val firestore: FirebaseFirestore) : FirestoreRepository {
    override suspend fun insertUser(user: UserData): Result<Unit> {
        return try {
            firestore.collection("users").add(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // Implement other Firestore-related methods
}