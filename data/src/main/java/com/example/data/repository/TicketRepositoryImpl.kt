package com.example.data.repository

import com.example.data.datasource.TicketDataSource
import com.example.data.model.TicketData
import com.example.data.model.toDomain
import com.example.domain.model.Ticket
import com.example.domain.repository.TicketRepository

class TicketRepositoryImpl(private val ticketDataSource: TicketDataSource) : TicketRepository {

    private var _ticketData: TicketData? = null

    override suspend fun processTicket(imageByte: ByteArray): Ticket {
        val result = ticketDataSource.processTicket(imageByte)
        _ticketData = result
        return result.toDomain()
    }

    override fun getTicketData(): Ticket? = _ticketData?.toDomain()
}