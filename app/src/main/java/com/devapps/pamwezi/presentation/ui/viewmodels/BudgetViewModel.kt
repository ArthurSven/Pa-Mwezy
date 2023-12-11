package com.devapps.pamwezi.presentation.ui.viewmodels


import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.devapps.pamwezi.data.InsertBudgetResult
import com.devapps.pamwezi.network.FirestoreDb
import com.devapps.pamwezi.data.InsertBudgetState
import com.devapps.pamwezi.domain.database.BudgetDao
import com.devapps.pamwezi.domain.database.BudgetDatabase
import com.devapps.pamwezi.domain.model.BudgetLocal
import com.devapps.pamwezi.domain.model.SignInState
import com.devapps.pamwezi.domain.repository.BudgetRepository
import com.devapps.pamwezi.domain.repository.BudgetRepositoryImpl
import com.devapps.pamwezi.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(private val budgetRepository: BudgetRepository)  : ViewModel()  {

    private val _state = MutableStateFlow(InsertBudgetState())
    val state = _state.asStateFlow()

    private val _userBudgets = MutableStateFlow<List<BudgetLocal>>(emptyList())
    val userBudgets: StateFlow<List<BudgetLocal>> = _userBudgets

    private val _createdBy = MutableStateFlow<String>("")
    val createdBy: StateFlow<String> = _createdBy

    fun setCreatedBy(username: String) {
        _createdBy.value = username
        getAllBudgetsByUser()
    }

    private fun getAllBudgetsByUser() {
        viewModelScope.launch {
            budgetRepository.getAllBudgetsByUser(_createdBy.value).collect { budgets ->
                _userBudgets.value = budgets
            }
        }
    }

    private fun onInsertBudgetResult(result: InsertBudgetResult) {
        _state.update {
            it.copy(
                isInsertedBudgetSuccessful = result.errorMessage == null,
                insertBudgetError = result.errorMessage
            )
        }
    }

    suspend fun insertBudget(budgetLocal: BudgetLocal) {
        try {
            val result = InsertBudgetResult(data = budgetLocal, errorMessage = null)
            budgetRepository.insertBudget(budgetLocal)
            onInsertBudgetResult(result)
        } catch (e: Exception) {
            val result = InsertBudgetResult(data = budgetLocal, errorMessage = e.message)
            onInsertBudgetResult(result)
        }

    }



    fun resetState() {
        _state.update { InsertBudgetState() }
    }
}



