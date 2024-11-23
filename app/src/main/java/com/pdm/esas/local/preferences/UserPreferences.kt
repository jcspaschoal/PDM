package com.pdm.esas.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        val USER_ID = stringPreferencesKey("USER_ID")
        val USER_NAME = stringPreferencesKey("USER_NAME")
        val USER_EMAIL = stringPreferencesKey("USER_EMAIL")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        val DEVICE_ID = stringPreferencesKey("DEVICE_ID")
        val USER_ROLES = stringPreferencesKey("USER_ROLES")
        val FIREBASE_TOKEN = stringPreferencesKey("FIREBASE_TOKEN")
        val FIREBASE_TOKEN_SENT = booleanPreferencesKey("FIREBASE_TOKEN_SENT")
    }

    // Generic getter for values
    private suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T? = null): T? {
        return dataStore.data.map { it[key] ?: defaultValue }.first()
    }

    // Generic setter for values
    private suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    // Generic remover for values
    private suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataStore.edit { it.remove(key) }
    }

    // User-specific operations
    suspend fun getUserId(): String? = getPreference(USER_ID)
    suspend fun setUserId(userId: String) = setPreference(USER_ID, userId)
    suspend fun removeUserId() = removePreference(USER_ID)

    suspend fun getUserName(): String? = getPreference(USER_NAME)
    suspend fun setUserName(userName: String) = setPreference(USER_NAME, userName)
    suspend fun removeUserName() = removePreference(USER_NAME)

    suspend fun getUserEmail(): String? = getPreference(USER_EMAIL)
    suspend fun setUserEmail(email: String) = setPreference(USER_EMAIL, email)
    suspend fun removeUserEmail() = removePreference(USER_EMAIL)

    suspend fun getAccessToken(): String? = getPreference(ACCESS_TOKEN)
    suspend fun setAccessToken(token: String) = setPreference(ACCESS_TOKEN, token)
    suspend fun removeAccessToken() = removePreference(ACCESS_TOKEN)

    suspend fun getRefreshToken(): String? = getPreference(REFRESH_TOKEN)
    suspend fun setRefreshToken(token: String) = setPreference(REFRESH_TOKEN, token)
    suspend fun removeRefreshToken() = removePreference(REFRESH_TOKEN)

    suspend fun getDeviceId(): String? = getPreference(DEVICE_ID)
    suspend fun setDeviceId(deviceId: String) = setPreference(DEVICE_ID, deviceId)

    suspend fun getFirebaseToken(): String? = getPreference(FIREBASE_TOKEN)
    suspend fun setFirebaseToken(token: String) = setPreference(FIREBASE_TOKEN, token)

    suspend fun isFirebaseTokenSent(): Boolean = getPreference(FIREBASE_TOKEN_SENT, false) ?: false
    suspend fun setFirebaseTokenSent() = setPreference(FIREBASE_TOKEN_SENT, true)
    suspend fun removeFirebaseTokenSent() = removePreference(FIREBASE_TOKEN_SENT)

    suspend fun getUserRoles(): String? = getPreference(USER_ROLES)
    suspend fun setUserRoles(roles: String) = setPreference(USER_ROLES, roles)
    suspend fun removeUserRoles() = removePreference(USER_ROLES)
}
