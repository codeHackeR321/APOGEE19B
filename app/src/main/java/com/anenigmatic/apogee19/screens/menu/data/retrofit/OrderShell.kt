package com.anenigmatic.apogee19.screens.menu.data.retrofit

import com.squareup.moshi.Json

data class OrderShell(
    @field:Json(name = "id") var shellId: Int,
    @field:Json(name = "orders") var order: List<Order>,
    @field:Json(name = "timestamp") var timestamp: String
)