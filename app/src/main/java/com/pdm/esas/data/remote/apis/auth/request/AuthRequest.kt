package com.pdm.esas.data.remote.apis.auth.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BasicAuthRequest(
    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String
)

