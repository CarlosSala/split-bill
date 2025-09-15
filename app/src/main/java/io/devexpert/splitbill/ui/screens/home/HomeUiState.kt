package io.devexpert.splitbill.ui.screens.home

data class HomeUiState(
    val scansLeft: Int = 0,
    val isProcessing: Boolean = false,
    val errorMessage: String? = null,
    val ticketProcessed: Boolean = false,
)