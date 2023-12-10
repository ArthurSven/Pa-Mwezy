package com.devapps.pamwezi.domain.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.devapps.pamwezi.domain.model.BudgetLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert
    suspend fun insertBudget(budgetLocal: BudgetLocal)

    @Query("SELECT * FROM budget_table WHERE createdBy = :createdBy ORDER BY id DESC")
    fun getBudgetsByUser(createdBy: String) : Flow<BudgetLocal>

    @Delete
    suspend fun deleteBudget(budgetLocal: BudgetLocal)
}