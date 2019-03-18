package com.anenigmatic.apogee19.screens.shared.data.retrofit

import com.squareup.moshi.Json

data class LoginResponse(
    @field:Json(name = "user_id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "JWT") val jwt: String,
    @field:Json(name = "qr_code") val qrCode: String
)