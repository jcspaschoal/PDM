package com.pdm.esas.data.remote.apis.user

import com.pdm.esas.data.remote.RequestHeaders
import com.pdm.esas.data.remote.apis.user.requests.FirebaseTokenRequest
import com.pdm.esas.data.remote.response.ApiGeneralResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PUT

interface UserApi {


    @PUT(Endpoints.PROFILE)
    @Headers(RequestHeaders.Key.AUTH_PROTECTED)
    suspend fun firebaseToken(@Body request: FirebaseTokenRequest): ApiGeneralResponse


}