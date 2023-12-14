package com.devapps.pamwezi.domain.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.devapps.pamwezi.domain.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM budget_expense WHERE budget_id = :budgetId")
    fun getAllExpensesByBudgetID(budgetId: Int) : Flow<List<Expense>>

    @Update
    fun updateExpense(expense: Expense)
}