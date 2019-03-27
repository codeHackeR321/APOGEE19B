package com.anenigmatic.apogee19.screens.menu.data.retrofit

import com.squareup.moshi.Json

data class OrderComfirmation(
    @field:Json(name = "display_message") val message: String
)