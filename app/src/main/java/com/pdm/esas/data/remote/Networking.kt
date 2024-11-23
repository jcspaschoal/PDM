package com.pdm.esas.data.remote

import com.pdm.esas.data.remote.interceptors.NetworkInterceptor
import com.pdm.esas.data.remote.interceptors.RefreshTokenInterceptor
import com.pdm.esas.data.remote.interceptors.RequestHeaderInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Cache
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object Networking {

    private const val NETWORK_CALL_TIMEOUT = 60

    fun <T> createService(baseUrl: String, client: OkHttpClient, service: Class<T>): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().build()
                )
            )
            .build()
            .create(service)


    fun createOkHttpClientForApis(
        networkInterceptor: NetworkInterceptor,
        headerInterceptor: RequestHeaderInterceptor,
        refreshTokenInterceptor: RefreshTokenInterceptor,
        cacheDir: File,
        cacheSize: Long
    ) = OkHttpClient.Builder()
        .cache(Cache(cacheDir, cacheSize))
        .addInterceptor(networkInterceptor)
        .addInterceptor(headerInterceptor)
        .addInterceptor(refreshTokenInterceptor)
        .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .build()

    fun createOkHttpClientForRefreshToken(
        networkInterceptor: NetworkInterceptor,
        headerInterceptor: RequestHeaderInterceptor,
    ) = OkHttpClient.Builder()
        .addInterceptor(networkInterceptor)
        .addInterceptor(headerInterceptor)
        .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .build()


}