package com.devapps.pamwezi.domain.repository

import androidx.room.Update
import com.devapps.pamwezi.domain.database.ExpenseDao
import com.devapps.pamwezi.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ExpenseRepository {
    suspend fun insertExpense(expense: Expense)

    suspend fun deleteExpense(expense: Expense)

    suspend fun getAllExpensesByBudgetID(budgetId: Int) : Flow<List<Expense>>

    fun updateExpense(expense: Expense)
}

class ExpenseRepositoryImpl @Inject constructor(private val expenseDao : ExpenseDao) :
    ExpenseRepository {
    override suspend fun insertExpense(expense: Expense) = expenseDao.insertExpense(expense)

    override suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)

    override suspend fun getAllExpensesByBudgetID(budgetId: Int): Flow<List<Expense>> =
        expenseDao.getAllExpensesByBudgetID(budgetId)

    override fun updateExpense(expense: Expense) = expenseDao.updateExpense(expense)

}