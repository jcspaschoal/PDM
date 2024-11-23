package com.pdm.esas.di.module

import android.content.Context
import com.pdm.esas.BuildConfig
import com.pdm.esas.data.remote.Networking
import com.pdm.esas.data.remote.apis.auth.AuthApi
import com.pdm.esas.data.remote.apis.auth.RefreshTokenApi
import com.pdm.esas.data.remote.apis.user.UserApi
import com.pdm.esas.data.remote.interceptors.NetworkInterceptor
import com.pdm.esas.data.remote.interceptors.RefreshTokenInterceptor
import com.pdm.esas.data.remote.interceptors.RequestHeaderInterceptor
import com.pdm.esas.di.qualifier.AccessTokenInfo
import com.pdm.esas.di.qualifier.ApiKeyInfo
import com.pdm.esas.di.qualifier.BaseUrl
import com.pdm.esas.di.qualifier.RefreshTokenInfo
import com.pdm.esas.local.preferences.UserPreferences
import com.pdm.esas.utils.common.ResultCallbackBlocking
import com.pdm.esas.utils.common.ResultFetcherBlocking
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRefreshTokenApi(
        @BaseUrl baseUrl: String,
        networkInterceptor: NetworkInterceptor,
        requestHeaderInterceptor: RequestHeaderInterceptor,
    ): RefreshTokenApi = Networking.createService(
        baseUrl,
        Networking.createOkHttpClientForRefreshToken(
            networkInterceptor,
            requestHeaderInterceptor,
        ),
        RefreshTokenApi::class.java
    )

    @Provides
    @Singleton
    fun provideApiOkHttpClient(
        @ApplicationContext context: Context,
        networkInterceptor: NetworkInterceptor,
        requestHeaderInterceptor: RequestHeaderInterceptor,
        refreshTokenInterceptor: RefreshTokenInterceptor
    ): OkHttpClient = Networking.createOkHttpClientForApis(
        networkInterceptor,
        requestHeaderInterceptor,
        refreshTokenInterceptor,
        context.cacheDir,
        50 * 1024 * 1024 // 50MB
    )

    @Provides
    @Singleton
    fun provideAuthApi(
        @BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient
    ): AuthApi = Networking.createService(
        baseUrl,
        okHttpClient,
        AuthApi::class.java
    )


    @Provides
    @Singleton
    fun provideUserApi(
        @BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient
    ): UserApi = Networking.createService(
        baseUrl,
        okHttpClient,
        UserApi::class.java
    )


    @Provides
    @Singleton
    @ApiKeyInfo
    fun provideApiKey(): String = BuildConfig.API_KEY

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    @AccessTokenInfo
    fun provideAccessToken(
        userPreferences: UserPreferences
    ): ResultFetcherBlocking<String> = object : ResultFetcherBlocking<String> {
        override fun fetch(): String? = runBlocking { userPreferences.getAccessToken() }
    }

    @Provides
    @Singleton
    @RefreshTokenInfo
    fun provideRefreshToken(
        userPreferences: UserPreferences
    ): ResultFetcherBlocking<String> = object : ResultFetcherBlocking<String> {
        override fun fetch(): String? = runBlocking { userPreferences.getRefreshToken() }
    }

    @Provides
    @Singleton
    @AccessTokenInfo
    fun provideAccessTokenSaveLambda(
        userPreferences: UserPreferences
    ): ResultCallbackBlocking<String> = object : ResultCallbackBlocking<String> {
        override fun onResult(result: String) =
            runBlocking { userPreferences.setAccessToken(result) }
    }

    @Provides
    @Singleton
    @RefreshTokenInfo
    fun provideRefreshTokenSaveLambda(
        userPreferences: UserPreferences
    ): ResultCallbackBlocking<String> = object : ResultCallbackBlocking<String> {
        override fun onResult(result: String) =
            runBlocking { userPreferences.setRefreshToken(result) }
    }
}