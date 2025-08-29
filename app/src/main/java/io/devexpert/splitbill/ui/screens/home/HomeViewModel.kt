package io.devexpert.splitbill.ui.screens.home

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.devexpert.splitbill.ui.core.ImageConverter
import io.devexpert.splitbill.usecases.DecrementScanCounterUseCase
import io.devexpert.splitbill.usecases.GetScansRemainingUseCase
import io.devexpert.splitbill.usecases.InitializeScanCounterUseCase
import io.devexpert.splitbill.usecases.ProcessTicketUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class HomeViewModel(
    private val initializeScanCounterUseCase: InitializeScanCounterUseCase,
    private val getScansRemainingUseCase: GetScansRemainingUseCase,
    private val decrementScanCounterUseCase: DecrementScanCounterUseCase,
    private val processTicketUseCase: ProcessTicketUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    init {
        initializeScanCounter()
        fetchScansRemaining()
    }

    fun initializeScanCounter() {
        viewModelScope.launch {
            initializeScanCounterUseCase()
        }
    }

    fun fetchScansRemaining() {
        viewModelScope.launch {
            getScansRemainingUseCase().collect { value ->
                _uiState.update { it.copy(scansLeft = value) }
            }
        }
    }

    fun decrementScanCounter() {
        viewModelScope.launch {
            decrementScanCounterUseCase()
        }
    }

    fun resetTicketProcessed() {
        _uiState.update { it.copy(tickedProcessed = false) }
    }

    fun processTicket(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            processTicketUseCase(ImageConverter.toResizedByteArray(bitmap))
            decrementScanCounter()
            _uiState.update { it.copy(isLoading = false, tickedProcessed = true) }
        }
    }
}