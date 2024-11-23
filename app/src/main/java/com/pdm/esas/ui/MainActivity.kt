package com.pdm.esas.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.pdm.esas.R
import com.pdm.esas.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()

    val MIME_TYPE_TEXT_PLAIN = "text/plain"


    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                ContextCompat.getColor(this, R.color.immersive_sys_ui)
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            SocialEsasApp(
                navigator = viewModel.navigator,
                loader = viewModel.loader,
                messenger = viewModel.messenger,
                finish = { finish() }
            )
        }
        handleIntent(intent)
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.run {
            when (action) {
                Intent.ACTION_SEND -> {
                    if (type == MIME_TYPE_TEXT_PLAIN) {
                        val text = getStringExtra(Intent.EXTRA_TEXT)
                    }
                }
                Intent.ACTION_VIEW -> {
                    viewModel.handleDeepLink(data)
                }
            }
        }
    }
}

