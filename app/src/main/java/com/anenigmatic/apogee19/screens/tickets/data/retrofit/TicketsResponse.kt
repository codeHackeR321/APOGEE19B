package com.anenigmatic.apogee19.screens.tickets.data.retrofit

import com.anenigmatic.apogee19.screens.tickets.core.Ticket
import com.squareup.moshi.Json

data class TicketsResponse(@field:Json(name = "combos") val combos: List<Ticket.ComboTicket>, @field:Json(name = "shows") val shows: List<Ticket.PlainTicket>)