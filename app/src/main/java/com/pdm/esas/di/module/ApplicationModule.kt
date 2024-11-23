package com.pdm.esas.di.module

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.pdm.esas.di.qualifier.DeviceIdInfo
import com.pdm.esas.local.preferences.UserPreferences
import com.pdm.esas.utils.common.ResultFetcherBlocking
import com.pdm.esas.utils.log.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    @DeviceIdInfo
    fun provideDeviceId(
        userPreferences: UserPreferences
    ): ResultFetcherBlocking<String> = object : ResultFetcherBlocking<String> {
        override fun fetch(): String? = runBlocking { userPreferences.getDeviceId() }
    }


    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = when {
                true -> 0
                else -> 60 * 60
            }
        })
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Logger.d(
                    "ApplicationModule",
                    "FirebaseRemoteConfig addOnCompleteListener isSuccessful updated: ${task.result}"
                )
            } else {
                Logger.e(
                    "ApplicationModule", "FirebaseRemoteConfig addOnCompleteListener Not Successful"
                )
            }
        }
        return remoteConfig
    }

}

