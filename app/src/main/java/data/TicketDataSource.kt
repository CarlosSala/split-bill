package data

import android.graphics.Bitmap
import domain.TicketData

interface TicketDataSource {

    suspend fun processTicket(image: Bitmap): TicketData
}