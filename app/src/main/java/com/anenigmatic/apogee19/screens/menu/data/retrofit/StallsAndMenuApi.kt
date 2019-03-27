package com.anenigmatic.apogee19.screens.menu.data.retrofit

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StallsAndMenuApi {

    @GET("2019/wallet/vendors")
    fun getStallsAndMenu() : Call<List<StallAndMenu>>

    @POST("2019/wallet/orders")
    fun placeOrder(@Header("Authorization") jwt: String, @Body body: CartOrder) : Call<OrderComfirmation>

    @GET("2019/wallet/orders")
    fun getPastOrders(@Header("Authorization") jwt: String) : Call<List<OrderShell>>

    @POST("2019/wallet/orders/make_otp_seen")
    fun requestOtpSeen(@Header("Authorization") jwt: String, @Body body: RequestBody) : Call<Unit>
}