package com.pdm.esas.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiGeneralResponse(

    @Json(name = "statusCode")
    val statusCode: String,

    @Json(name = "message")
    val message: String,
)