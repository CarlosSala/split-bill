package io.devexpert.splitbill.framework.home

sealed class HomeUIState {

    object Loading : HomeUIState()
    data class Success(val canScan: Boolean, val scansLeft: Int) : HomeUIState()
    data class Error(val message: String) : HomeUIState()
}