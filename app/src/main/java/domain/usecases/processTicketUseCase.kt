package domain.usecases

import data.TicketRepository

class ProcessTicketUseCase(private val ticketRepository: TicketRepository) {

    suspend operator fun invoke(imageByte: ByteArray){
        ticketRepository.processTicket(imageByte)
    }
}