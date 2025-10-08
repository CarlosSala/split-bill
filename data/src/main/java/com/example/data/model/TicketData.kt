package com.example.data.model

import com.example.domain.model.Item
import com.example.domain.model.Ticket
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketData(
    val items: List<TicketItem>,
    val total: Double
)

@Serializable
data class TicketItem(
    val name: String,
    @SerialName("count") val quantity: Int,
    @SerialName("price_per_unit") val price: Double
)


// mapper

fun TicketData.toDomain(): Ticket {
    return Ticket(
        items = items.map { it.toDomain() },
        total = total
    )
}

fun TicketItem.toDomain(): Item {
    return Item(
        name = name,
        quantity = quantity,
        price = price
    )
}