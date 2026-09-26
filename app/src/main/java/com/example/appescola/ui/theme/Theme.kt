package com.example.appescola.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/** Estado global simples de tema (alternado na tela de Configurações). */
object PulseThemeState {
    var escuro by mutableStateOf(true)
}

private val Escuro = darkColorScheme(
    primary = PulseOrange,
    onPrimary = Color(0xFF201000),
    secondary = PulseOrangeLight,
    onSecondary = Color(0xFF201000),
    background = PulseBackground,
    onBackground = PulseTextPrimary,
    surface = PulseSurface,
    onSurface = PulseTextPrimary,
    surfaceVariant = PulseSurfaceVariant,
    onSurfaceVariant = PulseTextSecondary,
    outline = PulseOutline,
    error = PulseRed,
    primaryContainer = PulseSurfaceVariant,
    onPrimaryContainer = PulseTextPrimary
)

private val Claro = lightColorScheme(
    primary = PulseOrange,
    onPrimary = Color.White,
    secondary = PulseOrangeLight,
    onSecondary = Color.White,
    background = PulseBackgroundLight,
    onBackground = PulseTextPrimaryLight,
    surface = PulseSurfaceLight,
    onSurface = PulseTextPrimaryLight,
    surfaceVariant = PulseSurfaceVariantLight,
    onSurfaceVariant = PulseTextSecondaryLight,
    outline = PulseOutlineLight,
    error = PulseRed,
    primaryContainer = PulseSurfaceVariantLight,
    onPrimaryContainer = PulseTextPrimaryLight
)

@Composable
fun AcademiaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (PulseThemeState.escuro) Escuro else Claro,
        typography = Typography,
        content = content
    )
}
