package io.devexpert.splitbill

/**
 * Singleton to share ticket data between screens
 */
object TicketDataHolder {
    private var _ticketData: TicketData? = null
    
    fun setTicketData(data: TicketData) {
        _ticketData = data
    }
    
    fun getTicketData(): TicketData? = _ticketData
    
    fun clearTicketData() {
        _ticketData = null
    }
} 