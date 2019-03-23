package com.anenigmatic.apogee19.screens.shared.data.storage

import com.anenigmatic.apogee19.screens.shared.core.Ticket

data class UserStorageData(
    val id: Long,
    val name: String,
    val jwt: String,
    val qrCode: String,
    val isBitsian: Boolean,
    val tickets: List<Ticket>,
    val avatarId: Long
)