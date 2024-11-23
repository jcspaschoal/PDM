package com.pdm.esas.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.esas.R
import com.pdm.esas.data.remote.response.ApiErrorResponse
import com.pdm.esas.ui.common.loader.Loader
import com.pdm.esas.ui.common.snackbar.Message
import com.pdm.esas.ui.common.snackbar.Messenger
import com.pdm.esas.ui.navigation.Destination
import com.pdm.esas.ui.navigation.Navigator
import com.pdm.esas.utils.log.Logger
import com.pdm.esas.utils.remote.toApiErrorResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseViewModel(
    private val loader: Loader,
    private val messenger: Messenger,
    private val navigator: Navigator
) : ViewModel() {

    companion object {
        const val TAG = "BaseViewModel"
    }

    protected fun launchNetwork(
        silent: Boolean = false,
        error: (ApiErrorResponse) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ) {
        if (!silent) {
            loader.start()
            viewModelScope.launch {
                try {
                    block()
                } catch (e: Throwable) {
                    if (e is CancellationException) return@launch
                    val errorResponse = e.toApiErrorResponse()
                    handleNetworkError(errorResponse)
                    error(errorResponse)
                    Logger.d(TAG, e)
                    Logger.record(e)
                } finally {
                    loader.stop()
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    block()
                } catch (e: Throwable) {
                    if (e is CancellationException) return@launch
                    val errorResponse = e.toApiErrorResponse()
                    error(errorResponse)
                    Logger.d(TAG, e)
                    Logger.record(e)
                }
            }
        }
    }

    private fun handleNetworkError(err: ApiErrorResponse) {
        when (err.status) {
            ApiErrorResponse.Status.HTTP_BAD_GATEWAY,
            ApiErrorResponse.Status.REMOTE_CONNECTION_ERROR -> {
                messenger.deliverRes(Message.error(R.string.server_connection_error))
                navigator.navigateTo(Destination.ServerUnreachable.route)
            }

            ApiErrorResponse.Status.NETWORK_CONNECTION_ERROR ->
                messenger.deliverRes(Message.error(R.string.no_internet_connection))

            ApiErrorResponse.Status.HTTP_UNAUTHORIZED ->
                messenger.deliver(Message.error(err.message))

            ApiErrorResponse.Status.HTTP_FORBIDDEN ->
                messenger.deliverRes(Message.error(R.string.permission_not_available))

            ApiErrorResponse.Status.HTTP_BAD_REQUEST ->
                err.message.let { messenger.deliver(Message.error(err.message)) }

            ApiErrorResponse.Status.HTTP_NOT_FOUND ->
                err.message.let { messenger.deliver(Message.error(err.message)) }

            ApiErrorResponse.Status.HTTP_INTERNAL_ERROR ->
                messenger.deliverRes(Message.error(R.string.network_internal_error))

            ApiErrorResponse.Status.HTTP_UNAVAILABLE -> {
                messenger.deliverRes(Message.error(R.string.network_server_not_available))
                navigator.navigateTo(Destination.ServerUnreachable.route)
            }

            ApiErrorResponse.Status.UNKNOWN ->
                messenger.deliverRes(Message.error(R.string.something_went_wrong))
        }
    }
}