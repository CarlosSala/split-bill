package com.example.domain.usecases

import com.example.domain.model.Ticket
import com.example.domain.repository.TicketRepository
import javax.inject.Inject

class GetTicketDataUseCase @Inject constructor(private val ticketRepository: TicketRepository) {

    operator fun invoke(): Ticket? = ticketRepository.getTicketData()
}