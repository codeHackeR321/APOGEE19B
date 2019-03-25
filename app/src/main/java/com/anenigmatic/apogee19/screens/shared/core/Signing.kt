package com.anenigmatic.apogee19.screens.shared.core

/**
 * Represents a ticket which the user has signed up for.
 * Entry in some events requires these signings.
 * */
data class Signing(val name: String, val quantity: Int)