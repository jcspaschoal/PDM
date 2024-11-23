package com.pdm.esas.data.remote.apis.auth

import com.google.rpc.context.AttributeContext
import com.pdm.esas.data.remote.RequestHeaders
import com.pdm.esas.data.remote.apis.auth.request.BasicAuthRequest
import com.pdm.esas.data.remote.response.ApiDataResponse
import com.pdm.esas.data.remote.response.ApiGeneralResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {

    @POST(Endpoints.AUTH_LOGIN_BASIC)
    @Headers(RequestHeaders.Key.AUTH_PUBLIC)
    suspend fun basicLogin(
        @Body request: BasicAuthRequest
    ): ApiDataResponse<AttributeContext.Auth>

    @DELETE(Endpoints.AUTH_LOGOUT)
    @Headers(RequestHeaders.Key.AUTH_PROTECTED)
    suspend fun logout(): ApiGeneralResponse


}