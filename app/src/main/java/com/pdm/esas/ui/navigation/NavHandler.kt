package com.pdm.esas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun NavHandler(
    navController: NavController,
    navigator: Navigator,
    finish: () -> Unit
) {
    LaunchedEffect(navigator.navigationEvents) {
        navigator.navigationEvents.collectLatest { event ->
            when (event) {
                is Navigator.NavigationEvent.Navigate -> {
                    if (event.popBackstack) navController.popBackStack()
                    navController.navigate(event.route)
                }
                is Navigator.NavigationEvent.DeepLink -> {
                    val uri = event.uri
                    if (navController.graph.hasDeepLink(uri)) {
                        val reached = navController.currentDestination?.hasDeepLink(uri) ?: false
                        if (!reached) {
                            if (event.popBackstack) navController.popBackStack()
                            navController.navigate(uri)
                        }
                    }
                }
                is Navigator.NavigationEvent.Back -> {
                    if (event.recreate) {
                        navController.previousBackStackEntry?.destination?.route?.let { route ->
                            navController.navigate(route) {
                                popUpTo(route) { inclusive = true }
                            }
                        }
                    } else {
                        navController.navigateUp()
                    }
                }
                is Navigator.NavigationEvent.End -> {
                    finish()
                }
            }
        }
    }
}
