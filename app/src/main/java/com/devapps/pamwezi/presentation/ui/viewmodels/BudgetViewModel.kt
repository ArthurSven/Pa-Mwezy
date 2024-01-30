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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.withContext
import javax.inject.Inject
sealed class BudgetItemEvent {
    data class NavigateToDetailScreen(val budgetId: Int) : BudgetItemEvent()
    data class ShowDeleteMessage(val budgetLocal: BudgetLocal) : BudgetItemEvent()

}
@HiltViewModel
class BudgetViewModel @Inject constructor(private val budgetRepository: BudgetRepository)  : ViewModel()  {

    private val _state = MutableStateFlow(InsertBudgetState())
    val state = _state.asStateFlow()

    private val _userBudgets = MutableStateFlow<List<BudgetLocal>>(emptyList())
    val userBudgets: StateFlow<List<BudgetLocal>> = _userBudgets

    private val _selectedBudget = MutableStateFlow<BudgetLocal?>(null)
    val selectedBudget: StateFlow<BudgetLocal?> = _selectedBudget

    private val _selectBudgetId = MutableStateFlow<Int?>(null)
    val selectBudgetId: StateFlow<Int?> = _selectBudgetId

    private val _selectBudgetByTitleAndName = MutableStateFlow<BudgetLocal?>(null)
    val selectBudgetByTitleAndName: StateFlow<BudgetLocal?> = _selectBudgetByTitleAndName

    private val _budgetEventChannel = Channel<BudgetItemEvent>()
    val budgetEventFlow: Flow<BudgetItemEvent> get() = _budgetEventChannel.receiveAsFlow()



    // Function to update the selected budget
    fun updateSelectedBudget(budget: BudgetLocal) {
        _selectedBudget.value = budget
    }

    private val _createdBy = MutableStateFlow<String>("")
    val createdBy: StateFlow<String> = _createdBy

    // MutableState to track delete operation result
    private val _isDeleteSuccessful = MutableStateFlow(false)
    val isDeleteSuccessful: StateFlow<Boolean> = _isDeleteSuccessful.asStateFlow()

    fun setCreatedBy(username: String) {
        _createdBy.value = username
        getAllBudgetsByUser()
    }

    fun setBudgetId(budgetId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _selectBudgetId.value = budgetId
                getBudgetByBudgetId(budgetId)
            }
        }
    }



    private fun getAllBudgetsByUser() {
        viewModelScope.launch {
            budgetRepository.getAllBudgetsByUser(_createdBy.value).collect { budgets ->
                _userBudgets.value = budgets
            }
        }
    }

    suspend fun getBudgetByBudgetId(budgetId: Int?) : BudgetLocal? {
        return budgetRepository.getBudgetById(budgetId)
    }

    fun onBudgetItemClick(budgetId: Int) {
        _selectBudgetId.value = budgetId

    }

    suspend fun deleteBudget(budgetLocal: BudgetLocal) {
        try {
            budgetRepository.deleteBudget(budgetLocal)
            _isDeleteSuccessful.value = true // Set to true on successful deletion
        } catch (e: Exception) {
            _isDeleteSuccessful.value = false // Set to false on deletion failure
            // Handle the exception if needed
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

    private fun sendEvent(event: BudgetItemEvent) {
        viewModelScope.launch {
            _budgetEventChannel.send(event)
        }
    }

    fun resetState() {
        _state.update { InsertBudgetState() }
    }

    // Function to reset isDeleteSuccessful state
    fun resetDeleteSuccessState() {
        _isDeleteSuccessful.value = false
    }
}



