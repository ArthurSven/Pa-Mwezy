package com.devapps.pamwezi.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapps.pamwezi.data.InsertBudgetResult
import com.devapps.pamwezi.data.InsertBudgetState
import com.devapps.pamwezi.data.InsertExpenseResult
import com.devapps.pamwezi.data.InsertExpenseState
import com.devapps.pamwezi.domain.model.BudgetLocal
import com.devapps.pamwezi.domain.model.Expense
import com.devapps.pamwezi.domain.repository.BudgetRepository
import com.devapps.pamwezi.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val budgetRepository: BudgetRepository) :
    ViewModel() {

        private val _expenseState = MutableStateFlow(InsertExpenseState())
        val expenseState = _expenseState.asStateFlow()

       private val _userExpenses = MutableStateFlow<List<Expense>>(emptyList())
        val userExpenses: StateFlow<List<Expense>> = _userExpenses

    private val _budgetId = MutableStateFlow<Int>(0)
    val budgetId: StateFlow<Int> = _budgetId

    // MutableState to track delete operation result
    private val _isDeleteSuccessful = MutableStateFlow(false)
    val isDeleteSuccessful: StateFlow<Boolean> = _isDeleteSuccessful.asStateFlow()

    suspend fun setBudgetId(id: Int?) {
        if (id != null) {
            _budgetId.value = id
        }
        getAllExpenseByBudgetId()
    }

    private fun onInsertExpenseResult(result: InsertExpenseResult) {
        _expenseState.update {
            it.copy(
                isInsertExpenseSuccessful = result.errorMessage == null,
                insertExpenseError = result.errorMessage
            )
        }
    }

    suspend fun insertExpense(expense: Expense) {
        val result = try {
            expenseRepository.insertExpense(expense)
            InsertExpenseResult(data = expense, errorMessage = null)
        } catch (e: Exception) {
            InsertExpenseResult(data = expense, errorMessage = e.message)
        }
        onInsertExpenseResult(result)
    }

    suspend fun getAllExpenseByBudgetId() {
        viewModelScope.launch {
            expenseRepository.getAllExpensesByBudgetID(_budgetId.value).collect { expenses ->
                _userExpenses.value = expenses
            }
        }
    }

    suspend fun deleteBudget(expense: Expense) {
        try {
            expenseRepository.deleteExpense(expense)
            _isDeleteSuccessful.value = true // Set to true on successful deletion
        } catch (e: Exception) {
            _isDeleteSuccessful.value = false // Set to false on deletion failure
            // Handle the exception if needed
        }
    }

    fun resetState() {
        _expenseState.update { InsertExpenseState() }
    }



}