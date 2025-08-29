package io.devexpert.splitbill.usecases

import io.devexpert.splitbill.data.repository.TicketRepository

class ProcessTicketUseCase(private val ticketRepository: TicketRepository) {

    suspend operator fun invoke(imageByte: ByteArray){
        ticketRepository.processTicket(imageByte)
    }
}