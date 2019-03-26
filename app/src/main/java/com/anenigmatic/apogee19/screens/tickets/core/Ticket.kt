package com.anenigmatic.apogee19.screens.tickets.core

import com.squareup.moshi.Json

/**
 * Represents a ticket which the user can purchase.
 * */
sealed class Ticket {

    data class PlainTicket(@field:Json(name = "id") val id: Long, @field:Json(name = "name") val name: String, @field:Json(name = "price") val price: Int) : Ticket()

    data class ComboTicket(val id: Long, val name: String, val componentNames: List<String>, val price: Int) : Ticket()
}