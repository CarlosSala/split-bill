package data

interface TicketDataSource {

    suspend fun processTicket(imageByte: ByteArray): TicketData
}