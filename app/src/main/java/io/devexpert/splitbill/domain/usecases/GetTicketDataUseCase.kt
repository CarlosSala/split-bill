package io.devexpert.splitbill.domain.usecases

import io.devexpert.splitbill.data.repository.TicketRepository
import io.devexpert.splitbill.data.model.TicketData
import javax.inject.Inject

class GetTicketDataUseCase @Inject constructor(private val ticketRepository: TicketRepository) {

    operator fun invoke(): TicketData? = ticketRepository.getTicketData()
}