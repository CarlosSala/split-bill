package io.devexpert.splitbill.ui.screens.receipt

import io.devexpert.splitbill.data.model.TicketData
import io.devexpert.splitbill.data.model.TicketItem

data class DetailUiState(
    val ticketData: TicketData? = null,
    val selectedQuantities: Map<TicketItem, Int> = emptyMap(),
    val paidQuantities: Map<TicketItem, Int> = emptyMap(),
    val availableItems: List<Pair<TicketItem, Int>> = emptyList(),
    val paidItems: List<Pair<TicketItem, Int>> = emptyList(),
    val selectedTotal: Double = 0.0
)
