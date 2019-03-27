package com.anenigmatic.apogee19.screens.menu.data.retrofit

import com.squareup.moshi.Json

data class Order(

    @field:Json(name = "id") var orderId: Int,
    @field:Json(name = "vendor") var vendor: Vendor,
    @field:Json(name ="price") var price: Int,
    @field:Json(name ="otp") var otp: Int,
    @field:Json(name = "status") var status: String,
    var showotp: Boolean = false,
    @field:Json(name ="items") var menu: List<ResponseOrderItem>
)