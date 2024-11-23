package com.pdm.esas.data.repository

import com.pdm.esas.data.model.Auth
import com.pdm.esas.data.model.Role
import com.pdm.esas.data.model.User
import com.pdm.esas.data.remote.apis.user.UserApi
import com.pdm.esas.data.remote.apis.user.requests.FirebaseTokenRequest
import com.pdm.esas.local.preferences.UserPreferences
import com.pdm.esas.utils.log.Logger
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userPreferences: UserPreferences
) {

    suspend fun userExists() = userPreferences.getUserId() != null

    suspend fun saveCurrentAuth(auth: Auth) {
        removeCurrentUser()
        val user = auth.user
        userPreferences.setUserId(user.id)
        user.name?.let { userPreferences.setUserName(it) }
        user.email?.let { userPreferences.setUserEmail(it) }

        val roles: String =
            try {
                Moshi.Builder().build().adapter<List<Role>>(
                    Types.newParameterizedType(
                        List::class.java,
                        Role::class.java
                    )
                ).toJson(user.roles)
            } catch (e: AssertionError) {
                Logger.record(e)
                "[]"
            }

        userPreferences.setUserRoles(roles)
        userPreferences.setAccessToken(auth.token.accessToken)
        userPreferences.setRefreshToken(auth.token.refreshToken)
    }

    suspend fun removeCurrentUser() {
        userPreferences.removeUserId()
        userPreferences.removeUserName()
        userPreferences.removeUserEmail()
        userPreferences.removeAccessToken()
        userPreferences.removeRefreshToken()
        userPreferences.removeUserRoles()
        userPreferences.removeFirebaseTokenSent()
    }

    suspend fun mustGetCurrentUser() = getCurrentUser()!!

    suspend fun getCurrentUser(): User? {
        val userId = userPreferences.getUserId()
        val userName = userPreferences.getUserName()
        val userEmail = userPreferences.getUserEmail()
        val rolesString = userPreferences.getUserRoles()

        if (userId !== null && rolesString != null) {
            val roles: List<Role> =
                try {
                    Moshi.Builder().build().adapter<List<Role>>(
                        Types.newParameterizedType(
                            List::class.java,
                            Role::class.java
                        )
                    ).fromJson(rolesString) ?: emptyList()
                } catch (e: AssertionError) {
                    Logger.record(e)
                    emptyList()
                }

            return User(
                userId, userName, userEmail, roles
            )
        }
        return null
    }


    suspend fun saveDeviceId(deviceId: String) = userPreferences.setDeviceId(deviceId)

    suspend fun getDeviceId() = userPreferences.getDeviceId()

    suspend fun sendFirebaseToken(token: String): Flow<String> =
        flow {
            emit(userApi.firebaseToken(FirebaseTokenRequest(token)))
        }.map { it.message }

    suspend fun getFirebaseToken(): String? = userPreferences.getFirebaseToken()

    suspend fun setFirebaseToken(token: String) = userPreferences.setFirebaseToken(token)

    suspend fun getFirebaseTokenSent(): Boolean = userPreferences.isFirebaseTokenSent()

    suspend fun setFirebaseTokenSent() = userPreferences.setFirebaseTokenSent()
}