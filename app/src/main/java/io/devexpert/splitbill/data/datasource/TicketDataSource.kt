package io.devexpert.splitbill.data.datasource

import io.devexpert.splitbill.data.model.TicketData

interface TicketDataSource {

    suspend fun processTicket(imageByte: ByteArray): TicketData
}