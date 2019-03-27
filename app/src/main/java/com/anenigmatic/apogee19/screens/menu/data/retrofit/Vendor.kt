package com.anenigmatic.apogee19.screens.menu.data.retrofit

import com.squareup.moshi.Json

data class Vendor(
    @field:Json(name = "id") var id: Int,
    @field:Json(name = "name") var name: String
)