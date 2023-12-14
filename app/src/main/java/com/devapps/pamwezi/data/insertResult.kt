package com.devapps.pamwezi.data

import com.devapps.pamwezi.domain.model.BudgetLocal
import com.devapps.pamwezi.domain.model.Expense

data class InsertBudgetResult (
        val data: BudgetLocal,
        val errorMessage: String?
    )

data class InsertExpenseResult(
    val data: Expense,
    var errorMessage: String?
)
