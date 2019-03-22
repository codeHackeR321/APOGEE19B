package com.anenigmatic.apogee19.screens.shared.core

/**
 * Represents a user of the app.
 *
 * @property id  is unique to a user. It can be used for transferring money.
 * @property balance  is the money(in INR) the user has  to use in the fest.
 * @property avatarId  is the id of the user's avatar. Avatars  are allotted
 *      at login. So, every logged-in user has an avatar at all times.
 * @property coins  is the number of coins  the user has. Coins are earnt by
 *      participating in events.
 * */
data class User(
    val id: Long,
    val name: String,
    val jwt: String,
    val qrCode: String,
    val isBitsian: Boolean,
    val balance: Int,
    val tickets: List<Ticket>,
    val avatarId: Long,
    val coins: Int
)