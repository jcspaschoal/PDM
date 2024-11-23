package com.pdm.esas.data.remote.apis.user.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FirebaseTokenRequest(

    @Json(name = "firebaseToken")
    val type: String
)