package io.devexpert.splitbill.ui.screens.receipt

import androidx.lifecycle.ViewModel
import io.devexpert.splitbill.data.model.TicketItem
import io.devexpert.splitbill.domain.usecases.GetTicketDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ReceiptViewModel(
    private val getTicketDataUseCase: GetTicketDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> get() = _uiState

    init {
        loadTicketData()
    }

    private fun loadTicketData() {
        val ticketData = getTicketDataUseCase()
        if (ticketData != null) {
            _uiState.update {
                it.copy(
                    ticketData = ticketData,
                    selectedQuantities = ticketData.items.associateWith { 0 },
                    paidQuantities = ticketData.items.associateWith { 0 },
                )
            }
        }
        updateUi()
    }

    fun onQuantityChange(item: TicketItem, newQuantity: Int) {
        _uiState.update { currentState ->
            val newSelectedQuantities = currentState.selectedQuantities.toMutableMap()
            newSelectedQuantities[item] = newQuantity
            currentState.copy(selectedQuantities = newSelectedQuantities)
        }
        updateUi()
    }

    fun onMarkAsPaid() {
        _uiState.update { currentState ->
            val newPaidQuantities = currentState.paidQuantities.toMutableMap()
            currentState.selectedQuantities.forEach { (item, selectedQty) ->
                if (selectedQty > 0) {
                    newPaidQuantities[item] = (newPaidQuantities[item] ?: 0) + selectedQty
                }
            }
            // Reset selected quantities
            val newSelectedQuantities = currentState.selectedQuantities.mapValues { 0 }

            currentState.copy(
                paidQuantities = newPaidQuantities,
                selectedQuantities = newSelectedQuantities
            )
        }
        updateUi()
    }

    private fun updateUi() {

        _uiState.update { currentState ->
            // if ticketData is null, return currentState without updating anything
            val ticketData = currentState.ticketData ?: return@update currentState

            // inside ticketData there's a list of TicketItem with the name, quantity and price
            val availableItems = ticketData.items.map { item ->
                // paidQuantities is a map of TicketItem to Int with the quantity of each item
                // if the item is not in paidQuantities, the quantity is 0
                val paidQuantities = currentState.paidQuantities[item] ?: 0
                // quantity total of item minus the quantity of items paid
                val availableQuantity = item.quantity - paidQuantities
                // "to" create a Pair of TicketItem and Int with the item and the available quantity
                // if the available quantity is greater than 0
                item to availableQuantity
            }.filter { it.second > 0 }

            // get the list of items that have been paid
            val paidItems = ticketData.items.map { item ->
                val paidQty = currentState.paidQuantities[item] ?: 0
                item to paidQty
            }.filter { it.second > 0 }

            // get the toral price of the selected items
            val selectedTotal = currentState.selectedQuantities.entries.sumOf { (item, quantity) ->
                item.price * quantity
            }

            currentState.copy(
                availableItems = availableItems,
                paidItems = paidItems,
                selectedTotal = selectedTotal
            )
        }
    }
}