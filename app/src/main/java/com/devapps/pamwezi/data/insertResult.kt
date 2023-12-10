package com.devapps.pamwezi.data

import com.devapps.pamwezi.domain.model.BudgetLocal

data class InsertBudgetResult (
        val data: BudgetLocal,
        val errorMessage: String?
    )
