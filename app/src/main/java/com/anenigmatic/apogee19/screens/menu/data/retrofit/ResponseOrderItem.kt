package com.anenigmatic.apogee19.screens.menu.data.retrofit

import com.squareup.moshi.Json

data class ResponseOrderItem(
    @field:Json(name = "id") var itemId: Int,
    @field:Json(name = "name") var name: String,
    @field:Json(name = "unit_price") var price: Int,
    @field:Json(name = "quantity") var quantity: Int
)