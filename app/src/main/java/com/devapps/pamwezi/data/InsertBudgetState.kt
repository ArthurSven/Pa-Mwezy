package com.devapps.pamwezi.data

data class InsertBudgetState(
    val isInsertedBudgetSuccessful: Boolean = false,
    val insertBudgetError: String? = null
)

data class InsertExpenseState(
    val isInsertExpenseSuccessful: Boolean = false,
    val insertExpenseError: String? = null
)
