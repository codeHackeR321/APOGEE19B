package com.anenigmatic.apogee19.screens.shared.data.retrofit

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    @POST("2019/wallet/auth")
    fun login(@Body body: RequestBody): Single<LoginResponse>

    @POST("2019/wallet/auth/avatar")
    fun chooseAvatar(@Header("Authorization") jwt: String, @Body body: RequestBody): Completable

    @POST("2019/wallet/monetary/add/swd")
    fun addMoney(@Header("Authorization") jwt: String, @Body body: RequestBody): Completable

    @POST("2019/wallet/monetary/transfer")
    fun transferMoney(@Header("Authorization") jwt: String, @Body body: RequestBody): Completable

    @POST("2019/wallet/auth/qr-code/refresh")
    fun refreshQrCode(@Header("Authorization") jwt: String, @Body body: RequestBody): Single<RefreshQrCodeResponse>
}