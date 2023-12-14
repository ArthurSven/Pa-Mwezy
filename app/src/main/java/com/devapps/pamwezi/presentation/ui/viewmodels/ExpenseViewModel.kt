package com.devapps.pamwezi.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devapps.pamwezi.data.InsertBudgetResult
import com.devapps.pamwezi.data.InsertExpenseResult
import com.devapps.pamwezi.data.InsertExpenseState
import com.devapps.pamwezi.domain.model.BudgetLocal
import com.devapps.pamwezi.domain.model.Expense
import com.devapps.pamwezi.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val expenseRepository: ExpenseRepository) :
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

    fun setCreatedBy(id: Int) {
        _budgetId.value = id
    }

    private fun onInsertExpenseResult(result: InsertExpenseResult) {
        _expenseState.update {
            it.copy(
                isInsertExpenseSuccessful = result.errorMessage == null,
                insertExpenseError = result.errorMessage
            )
        }
    }

    suspend fun insertBudget(expense: Expense) {
        try {
            val result = InsertExpenseResult(data = expense, errorMessage = null)
            expenseRepository.insertExpense(expense)
            onInsertExpenseResult(result)
        } catch (e: Exception) {
            val result = InsertExpenseResult(data = expense, errorMessage = e.message)
            onInsertExpenseResult(result)
        }

    }

    private fun getAllExpensesByBudgetID() {
        viewModelScope.launch {
            expenseRepository.getAllExpensesByBudgetID(_budgetId.value).collect{
                expenses ->
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



}