package com.pdm.esas.ui.navigation

import android.net.Uri
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@ActivityRetainedScoped
class Navigator @Inject constructor() {

    sealed class NavigationEvent {
        data class Navigate(val route: String, val popBackstack: Boolean = false) : NavigationEvent()
        data class DeepLink(val uri: Uri, val popBackstack: Boolean = false) : NavigationEvent()
        data class Back(val recreate: Boolean = false) : NavigationEvent()
        object End : NavigationEvent()
    }

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun navigateTo(route: String, popBackstack: Boolean = false) {
        _navigationEvents.tryEmit(NavigationEvent.Navigate(route, popBackstack))
    }

    fun navigateTo(uri: Uri, popBackstack: Boolean = false) {
        _navigationEvents.tryEmit(NavigationEvent.DeepLink(uri, popBackstack))
    }

    fun goBack(recreate: Boolean = false) {
        _navigationEvents.tryEmit(NavigationEvent.Back(recreate))
    }

    fun finish() {
        _navigationEvents.tryEmit(NavigationEvent.End)
    }
}
