package com.pdm.esas.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object Destination {
    data object Login : Screen("login")
    data object Splash : Screen("splash")
    data object Report: DynamicScreen("report", "reportId")
    data object ServerUnreachable : Screen("server-unreachable")


    data object Calendar : Screen("calendar") {
        data object Task : DynamicScreen("calendar/task", "taskId")
    }

    abstract class Screen(baseRoute: String) {
        companion object {
            const val BASE_DEEPLINK_URL = "app://esas"
        }

        open val route = baseRoute
        open val deeplink = "$BASE_DEEPLINK_URL/$baseRoute"
    }

    abstract class DynamicScreen(
        private val baseRoute: String,
        private val routeArgName: String,
    ) : Screen(baseRoute) {

        val navArguments = listOf(navArgument(routeArgName) { type = NavType.StringType })

        override val route = "$baseRoute/{$routeArgName}"
        override val deeplink = "$BASE_DEEPLINK_URL/$baseRoute/{$routeArgName}"

        fun dynamicRoute(param: String) = "$baseRoute/$param"

        fun dynamicDeeplink(param: String) = "$BASE_DEEPLINK_URL/$baseRoute/${param}"
    }
}