package com.example.data.datasource

import com.example.data.model.TicketData

interface TicketDataSource {

    suspend fun processTicket(imageByte: ByteArray): TicketData
}