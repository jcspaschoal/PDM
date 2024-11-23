package com.pdm.esas.data.remote.interceptors

import com.pdm.esas.utils.remote.NetworkHelper
import com.pdm.esas.utils.remote.NoConnectivityException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response



@Singleton
class NetworkInterceptor @Inject constructor(
    private val networkHelper: NetworkHelper
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkHelper.isNetworkConnected())
            throw NoConnectivityException()
        return chain.proceed(chain.request())
    }
}
