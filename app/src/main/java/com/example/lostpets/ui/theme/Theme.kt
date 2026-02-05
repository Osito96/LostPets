package com.example.lostpets.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val LightColorScheme = lightColorScheme(
    primary = AmarilloPrimario,
    onPrimary = NegroTexto,
    primaryContainer = AmarilloContainer,
    background = BlancoFondo,
    onBackground = NegroTexto,
    surface = BlancoFondo,
    onSurface = NegroTexto
)


private val DarkColorScheme = darkColorScheme(
    primary = MoradoPrimario,
    onPrimary = BlancoTexto,
    primaryContainer = MoradoContainer,
    background = NegroFondo,
    onBackground = BlancoTexto,
    surface = Color(0xFF1E1E1E),
    onSurface = BlancoTexto
)


var isDarkModeGlobal by mutableStateOf(false)

@Composable
fun LostPetsTheme(

    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (isDarkModeGlobal) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkModeGlobal
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}