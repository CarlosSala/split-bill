package io.devexpert.splitbill.ui.screens.receipt

import io.devexpert.splitbill.ui.model.ItemUi
import io.devexpert.splitbill.ui.model.TicketUi


data class DetailUiState(
    val ticketData: TicketUi? = null,
    val selectedQuantities: Map<ItemUi, Int> = emptyMap(),
    val paidQuantities: Map<ItemUi, Int> = emptyMap(),
    val availableItems: List<Pair<ItemUi, Int>> = emptyList(),
    val paidItems: List<Pair<ItemUi, Int>> = emptyList(),
    val selectedTotal: Double = 0.0
)
