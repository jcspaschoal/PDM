package com.pdm.esas.ui.common.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.Composable
import com.pdm.esas.ui.theme.black
import com.pdm.esas.ui.theme.info
import com.pdm.esas.ui.theme.success
import com.pdm.esas.ui.theme.warning

@Composable
fun AppSnackbar(
    snackbarHostState: SnackbarHostState,
    messenger: Messenger
) {
    MessageHandler(snackbarHostState, messenger)

    val messageType = messenger.messageType.collectAsStateWithLifecycle()

    val color = when (messageType.value) {
        Message.Type.SUCCESS -> MaterialTheme.colorScheme.success
        Message.Type.ERROR -> MaterialTheme.colorScheme.error
        Message.Type.WARNING -> MaterialTheme.colorScheme.warning
        Message.Type.INFO -> MaterialTheme.colorScheme.info
    }

    SnackbarHost(hostState = snackbarHostState) {
        Snackbar(
            snackbarData = it,
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.black,
            actionColor = MaterialTheme.colorScheme.black
        )
    }
}