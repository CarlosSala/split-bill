package domain.usecases

import data.TicketRepository
import data.TicketData

class GetTicketDataUseCase(private val ticketRepository: TicketRepository) {

    operator fun invoke(): TicketData? = ticketRepository.getTicketData()
}