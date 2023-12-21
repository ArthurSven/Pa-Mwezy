package com.devapps.pamwezi.domain.repository

import com.devapps.pamwezi.data.remote.Budget
import com.devapps.pamwezi.domain.database.BudgetDao
import com.devapps.pamwezi.domain.model.BudgetLocal
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface BudgetRepository {

    suspend fun insertBudget(budgetLocal: BudgetLocal)

    fun getAllBudgetsByUser(createdBy: String) : Flow<List<BudgetLocal>>

    suspend fun deleteBudget(budgetLocal: BudgetLocal)

    fun getBudgetById(id: Int?): BudgetLocal?

   fun getBudgetByNameAndTitle(createdBy: String, budgetTitle: String) : BudgetLocal
}

class BudgetRepositoryImpl @Inject constructor(private val budgetDao: BudgetDao) : BudgetRepository {
    override suspend fun insertBudget(budgetLocal: BudgetLocal) {
        return budgetDao.insertBudget(budgetLocal)
    }

    override fun getAllBudgetsByUser(createdBy: String): Flow<List<BudgetLocal>> {
        return budgetDao.getBudgetsByUser(createdBy)
    }


    override suspend fun deleteBudget(budgetLocal: BudgetLocal) {
        return budgetDao.deleteBudget(budgetLocal)
    }

    override fun getBudgetById(id: Int?): BudgetLocal = budgetDao.getBudgetById(id)



    override fun getBudgetByNameAndTitle(createdBy: String, budgetTitle: String): BudgetLocal {
        return budgetDao.getBudgetByNameAndTitle(createdBy, budgetTitle)
    }
}