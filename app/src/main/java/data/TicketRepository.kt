package data

import android.graphics.Bitmap
import domain.TicketData

class TicketRepository(private val ticketDataSource: TicketDataSource) {

    private var _ticketData: TicketData? = null

    suspend fun processTicket(image: Bitmap): TicketData {
        val result = ticketDataSource.processTicket(image)
        _ticketData = result
        return result
    }

    fun getTicketData(): TicketData? = _ticketData
}