package com.anenigmatic.apogee19.screens.tickets.data.retrofit

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TicketsApi {

    @GET("2019/tickets-manager/shows")
    fun getPurchaseableTickets(@Header("Authorization") jwt: String): Single<TicketsResponse>

    @POST("2019/tickets-manager/signup")
    fun purchaseTicket(@Header("Authorization") jwt: String, @Body body: RequestBody): Completable
}