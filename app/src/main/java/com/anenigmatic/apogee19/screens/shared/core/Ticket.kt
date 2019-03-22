package com.anenigmatic.apogee19.screens.shared.core

/**
 * Represents a ticket which the user has. Entry in some events requires
 * these tickets.
 * */
data class Ticket(val name: String, val quantity: Int)