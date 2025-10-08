package com.example.domain.repository

import com.example.domain.model.Ticket


interface TicketRepository {
    suspend fun processTicket(imageByte: ByteArray): Ticket

    fun getTicketData(): Ticket?
}