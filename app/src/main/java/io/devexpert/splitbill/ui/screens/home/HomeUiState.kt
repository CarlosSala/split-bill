package io.devexpert.splitbill.ui.screens.home

/*
sealed class HomeUIState {

    object Loading : HomeUIState()
    data class Success(val canScan: Boolean, val scansLeft: Int) : HomeUIState()
    data class Error(val message: String) : HomeUIState()
}*/

data class HomeUiState(
    val isLoading: Boolean = false,
    val tickedProcessed: Boolean = false,
    val scansLeft: Int = 0,
    val errorMessage: String? = null
)