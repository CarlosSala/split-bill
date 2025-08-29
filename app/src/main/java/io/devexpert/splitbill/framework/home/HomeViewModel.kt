package io.devexpert.splitbill.framework.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.devexpert.splitbill.usecases.DecrementScanCounterUseCase
import io.devexpert.splitbill.usecases.GetScansRemainingUseCase
import io.devexpert.splitbill.usecases.InitializeScanCounterUseCase
import io.devexpert.splitbill.usecases.ProcessTicketUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeViewModelFactory(
    private val initializeScanCounterUseCase: InitializeScanCounterUseCase,
    private val getScansRemainingUseCase: GetScansRemainingUseCase,
    private val decrementScanCounterUseCase: DecrementScanCounterUseCase,
    private val processTicketUseCase: ProcessTicketUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                initializeScanCounterUseCase, getScansRemainingUseCase,
                decrementScanCounterUseCase, processTicketUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class HomeViewModel(
    private val initializeScanCounterUseCase: InitializeScanCounterUseCase,
    private val getScansRemainingUseCase: GetScansRemainingUseCase,
    private val decrementScanCounterUseCase: DecrementScanCounterUseCase,
    private val processTicketUseCase: ProcessTicketUseCase

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

    fun decrementScanCounter() {
        viewModelScope.launch(Dispatchers.IO) {
            decrementScanCounterUseCase()
        }
    }

    fun processTicket(imageByte: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            processTicketUseCase(imageByte)
        }
    }
}