package io.devexpert.splitbill.usecases

import io.devexpert.splitbill.data.repository.TicketRepository
import io.devexpert.splitbill.data.model.TicketData

class GetTicketDataUseCase(private val ticketRepository: TicketRepository) {

    operator fun invoke(): TicketData? = ticketRepository.getTicketData()
}