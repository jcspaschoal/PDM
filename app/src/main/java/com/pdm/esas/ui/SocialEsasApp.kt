package com.pdm.esas.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.pdm.esas.ui.common.loader.Loader
import com.pdm.esas.ui.common.loader.Loading
import com.pdm.esas.ui.common.snackbar.AppSnackbar
import com.pdm.esas.ui.common.snackbar.Messenger
import com.pdm.esas.ui.navigation.NavGraph
import com.pdm.esas.ui.navigation.Navigator
import com.pdm.esas.ui.theme.AppTheme


@Composable
fun SocialEsasApp(
    loader: Loader,
    messenger: Messenger,
    navigator: Navigator,
    finish: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AppTheme {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier.imePadding(),
            snackbarHost = { AppSnackbar(snackbarHostState, messenger) },
        ) { innerPaddingModifier ->
            NavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPaddingModifier),
                navigator = navigator,
                finish = finish
            )
            Loading(
                modifier = Modifier
                    .padding(innerPaddingModifier)
                    .fillMaxWidth(),
                loader = loader
            )
        }
    }
}