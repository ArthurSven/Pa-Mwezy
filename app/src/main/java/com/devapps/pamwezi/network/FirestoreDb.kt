package com.devapps.pamwezi.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devapps.pamwezi.data.remote.Budget
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirestoreDb {

    private val budgetCollectionRef = Firebase.firestore.collection("budget")

    fun insertBudget(
        budgetTitle: String,
        budgetAmount: Double,
        month: String,
        createdBy: String
        ) : LiveData<Result<Unit>> {

        val resultLiveData = MutableLiveData<Result<Unit>>()

        try {
            val budget = Budget(
                title = budgetTitle,
                amount = budgetAmount,
                month = month,
                createdBy = createdBy
            )

            budgetCollectionRef.add(budget)
                .addOnSuccessListener {
                    resultLiveData.value = Result.success(Unit)
                }
                .addOnFailureListener {e ->
                    resultLiveData.value = Result.failure(e)
                }
        } catch (e: Exception) {
            resultLiveData.value = Result.failure(e)
        }

        return resultLiveData
    }

    suspend fun getBudgetByUsername(createdBy: String) {

        val getUserBudget = budgetCollectionRef
            .whereEqualTo("createdBy", createdBy)
            .orderBy("Document ID")
            .get()
            .await()

        val sb = StringBuffer()
        for (document in getUserBudget) {

        }
    }
}