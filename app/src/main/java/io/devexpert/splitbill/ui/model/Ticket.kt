package io.devexpert.splitbill.ui.model

import com.example.domain.model.Item
import com.example.domain.model.Ticket


data class TicketUi(
    val items: List<ItemUi>,
    val total: Double
)

data class ItemUi(
    val name: String,
    val quantity: Int,
    val price: Double
)

// mapper

fun Ticket.toUi(): TicketUi {
    return TicketUi(
        items = items.map { it.toUi() },
        total = total
    )
}

fun Item.toUi(): ItemUi {
    return ItemUi(
        name = name,
        quantity = quantity,
        price = price
    )
}