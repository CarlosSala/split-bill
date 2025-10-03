package io.devexpert.splitbill.domain.usecases

import io.devexpert.splitbill.data.model.TicketData
import io.devexpert.splitbill.data.repository.TicketRepository
import javax.inject.Inject

class ProcessTicketUseCase @Inject constructor(private val ticketRepository: TicketRepository) {

    suspend operator fun invoke(imageBytes: ByteArray): TicketData {
        return ticketRepository.processTicket(imageBytes)
    }
}