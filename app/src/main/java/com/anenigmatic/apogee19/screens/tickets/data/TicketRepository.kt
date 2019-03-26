package com.anenigmatic.apogee19.screens.tickets.data

import com.anenigmatic.apogee19.screens.tickets.core.Ticket
import io.reactivex.Completable
import io.reactivex.Flowable

interface TicketRepository {

    /**
     * Gives the list of all tickets available for the
     * user to purchase.
     * */
    fun getPurchaseableTickets(): Flowable<List<Ticket>>

    /**
     * Purchase the  passed-in  quantity of the ticket
     * having the passed-in ticket id.
     * */
    fun purchaseTicket(ticketId: Long, isCombo: Boolean, quantity: Int): Completable
}