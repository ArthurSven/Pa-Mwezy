package com.devapps.pamwezi.data

data class InsertBudgetState(
    val isInsertedBudgetSuccessful: Boolean = false,
    val insertBudgetError: String? = null
)
