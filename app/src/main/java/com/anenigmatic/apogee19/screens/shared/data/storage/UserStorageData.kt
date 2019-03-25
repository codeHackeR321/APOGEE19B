package com.anenigmatic.apogee19.screens.shared.data.storage

import com.anenigmatic.apogee19.screens.shared.core.Signing

data class UserStorageData(
    val id: Long,
    val name: String,
    val jwt: String,
    val qrCode: String,
    val isBitsian: Boolean,
    val signings: List<Signing>,
    val avatarId: Long
)