package com.anenigmatic.apogee19.screens.menu.data.retrofit

import com.anenigmatic.apogee19.screens.menu.data.room.StallItem
import com.squareup.moshi.Json

data class StallAndMenu(
    @field:Json(name = "id")var stallId: Int,
    @field:Json(name = "name")var name: String?,
    @field:Json(name = "description") var description: String?,
    @field:Json(name = "closed")var isClosed: Boolean,
    @field:Json(name = "menu") var menu: List<StallItem>
)