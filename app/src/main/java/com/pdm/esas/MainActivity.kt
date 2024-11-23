package com.pdm.esas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pdm.esas.ui.theme.SocialEsasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialEsasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun SocialEsasApp() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    MyShoppingListTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }

        LaunchedEffect(Unit) {
            val auth = Firebase.auth
            val currentUser = auth.currentUser
            scope.launch {
                if (currentUser != null) {
                    navController.navigate(Screen.ListTypes.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }
    }
}


sealed class Screen(val route: String) {
    data object Login : Screen("login")
}


@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginView(onLoginSuccess = {
                navController.navigate(Screen.ListTypes.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }
        composable(Screen.ListTypes.route) {
            ListTypesView(onAddListType = {
                navController.navigate(Screen.AddListType.route)
            })
        }
        composable(Screen.AddListType.route) {
            AddListTypeView(navController = navController)
        }
    }
}
