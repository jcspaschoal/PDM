package com.pdm.esas.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

// Cores principais
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650A4)
val PurpleGrey40 = Color(0xFF625B71)
val Pink40 = Color(0xFF7D5260)

// Cores de estado (sucesso, alerta, informação)
val SuccessColor = Color(0xFF8DB333)
val WarningColor = Color(0xFFFFA086)
val InfoColor = Color(0xFF72A2FF)

// Branco e preto
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)

// Definição de esquemas de cores
val LightColors = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = White,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = Black,
    onSurface = Black
)

val DarkColors = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Black,
    surface = Black,
    onPrimary = Black,
    onSecondary = Black,
    onTertiary = Black,
    onBackground = White,
    onSurface = White
)

val ColorScheme.white: Color
    get() = Color(0xFFFFFFFF)

val ColorScheme.black: Color
    get() = Color(0xFF000000)

val ColorScheme.success: Color
    get() = Color(0xFF8DB333)

val ColorScheme.warning: Color
    get() = Color(0xFFFFA086)

val ColorScheme.info: Color
    get() = Color(0xFF72A2FF)

@Composable
fun ColorScheme.compositedOnSurface(alpha: Float): Color {
    return onSurface.copy(alpha = alpha).compositeOver(surface)
}