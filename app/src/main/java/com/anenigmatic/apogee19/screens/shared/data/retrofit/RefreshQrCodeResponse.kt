package com.anenigmatic.apogee19.screens.shared.data.retrofit

import com.squareup.moshi.Json

data class RefreshQrCodeResponse(@field:Json(name = "qr_code") val qrCode: String)