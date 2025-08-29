package io.devexpert.splitbill.framework.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.devexpert.splitbill.usecases.GetScansRemainingUseCase
import io.devexpert.splitbill.usecases.InitializeScanCounterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeViewModelFactory(
    private val initializeScanCounterUseCase: InitializeScanCounterUseCase,
    private val getScansRemainingUseCase: GetScansRemainingUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(initializeScanCounterUseCase, getScansRemainingUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class HomeViewModel(
    private val initializeScanCounterUseCase: InitializeScanCounterUseCase,
    private val getScansRemainingUseCase: GetScansRemainingUseCase,
    /*  private val decrementScanCounterUseCase: DecrementScanCounterUseCase,
        private val ticketRepository: TicketRepository*/

) : ViewModel() {


    private val _scansLeft = MutableStateFlow<Int>(0)
    val scansLeft: StateFlow<Int> = _scansLeft

    init {
        fetchScansRemaining()
    }

    fun initializeScanCounter() {
        viewModelScope.launch(Dispatchers.IO) {
            initializeScanCounterUseCase()
        }
    }


    fun fetchScansRemaining() {
        viewModelScope.launch(Dispatchers.IO) {
            getScansRemainingUseCase().collect { value ->
                _scansLeft.value = value
            }
        }
    }
}