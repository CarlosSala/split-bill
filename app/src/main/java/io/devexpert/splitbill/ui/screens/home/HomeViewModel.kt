package io.devexpert.splitbill.ui.screens.home

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.devexpert.splitbill.ui.core.ImageConverter
import io.devexpert.splitbill.domain.usecases.DecrementScanCounterUseCase
import io.devexpert.splitbill.domain.usecases.GetScansRemainingUseCase
import io.devexpert.splitbill.domain.usecases.InitializeScanCounterUseCase
import io.devexpert.splitbill.domain.usecases.ProcessTicketUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val processTicketUseCase: ProcessTicketUseCase,
    private val initializeScanCounterUseCase: InitializeScanCounterUseCase,
    private val getScansRemainingUseCase: GetScansRemainingUseCase,
    private val decrementScanCounterUseCase: DecrementScanCounterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        initializeScanCounter()
        loadScansRemaining()
    }

    private fun initializeScanCounter() {
        viewModelScope.launch {
            initializeScanCounterUseCase()
        }
    }

    private fun loadScansRemaining() {
        viewModelScope.launch {
            getScansRemainingUseCase().collect { scans ->
                _uiState.value = _uiState.value.copy(scansLeft = scans)
            }
        }
    }

    fun processTicket(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessing = true, errorMessage = null)
            try {
                val imageBytes = ImageConverter.toResizedByteArray(bitmap)
                processTicketUseCase(imageBytes)
                decrementScanCounterUseCase()
                _uiState.value = _uiState.value.copy(isProcessing = false, ticketProcessed = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isProcessing = false, errorMessage = e.message)
            }
        }
    }

    fun resetTicketProcessed() {
        _uiState.value = _uiState.value.copy(ticketProcessed = false)
    }
}