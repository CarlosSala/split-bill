package com.example.domain.usecases


import com.example.domain.model.Ticket
import com.example.domain.repository.TicketRepository
import javax.inject.Inject


class ProcessTicketUseCase @Inject constructor(private val ticketRepository: TicketRepository) {

    suspend operator fun invoke(imageBytes: ByteArray): Ticket {
        return ticketRepository.processTicket(imageBytes)
    }
}