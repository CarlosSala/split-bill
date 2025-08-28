package io.devexpert.splitbill.data.repository

import io.devexpert.splitbill.data.datasource.TicketDataSource
import io.devexpert.splitbill.data.model.TicketData

class TicketRepository(private val ticketDataSource: TicketDataSource) {

    private var _ticketData: TicketData? = null

    suspend fun processTicket(imageByte: ByteArray): TicketData {
        val result = ticketDataSource.processTicket(imageByte)
        _ticketData = result
        return result
    }

    fun getTicketData(): TicketData? = _ticketData
}