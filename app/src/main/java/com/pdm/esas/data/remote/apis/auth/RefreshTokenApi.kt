package com.pdm.esas.data.remote.apis.auth


import com.pdm.esas.data.model.Token
import com.pdm.esas.data.remote.RequestHeaders
import com.pdm.esas.data.remote.apis.auth.request.RefreshTokenRequest
import com.pdm.esas.data.remote.response.ApiDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RefreshTokenApi {

    @POST(Endpoints.AUTH_REFRESH_TOKEN)
    @Headers(RequestHeaders.Key.AUTH_PROTECTED)
    fun refreshToken(@Body request: RefreshTokenRequest): Call<ApiDataResponse<Token>>

}