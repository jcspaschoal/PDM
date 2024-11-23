package com.pdm.esas.ui

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.pdm.esas.data.repository.UserRepository
import com.pdm.esas.ui.base.BaseViewModel
import com.pdm.esas.ui.common.loader.Loader
import com.pdm.esas.ui.common.snackbar.Messenger
import com.pdm.esas.ui.navigation.Destination
import com.pdm.esas.ui.navigation.Navigator
import com.pdm.esas.utils.remote.ForcedLogout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    forcedLogout: ForcedLogout,
    userRepository: UserRepository,
    val loader: Loader,
    val messenger: Messenger,
    val navigator: Navigator,
) : BaseViewModel(loader, messenger, navigator) {

    init {
        viewModelScope.launch {
            forcedLogout.state
                .collect {
                    if (it) {
                        userRepository.removeCurrentUser()
                        navigator.navigateTo(Destination.Login.route, true)
                    }
                }
        }
    }


    fun handleDeepLink(uri: Uri?) {
        uri?.let { navigator.navigateTo(uri) }
    }
}