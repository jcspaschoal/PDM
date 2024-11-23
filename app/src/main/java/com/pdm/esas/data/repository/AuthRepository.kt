package com.pdm.esas.data.repository

import com.pdm.esas.data.model.Auth
import com.pdm.esas.data.remote.apis.auth.AuthApi
import com.pdm.esas.data.remote.apis.auth.request.BasicAuthRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
) {
    suspend fun basicLogin(email: String, password: String): Flow<Auth> =
        flow {
            emit(authApi.basicLogin(BasicAuthRequest(email, password)))
        }.map { it.data }

    suspend fun logout(): Flow<String> =
        flow {
            emit(authApi.logout())
        }.map { it.message }


}
