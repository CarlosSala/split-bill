package com.example.domain.model


data class Ticket(
    val items: List<Item>,
    val total: Double
)

data class Item(
    val name: String,
   val quantity: Int,
   val price: Double
)