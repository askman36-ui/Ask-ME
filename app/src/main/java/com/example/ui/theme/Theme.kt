package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BrandIndigoSecondary,
    onPrimary = Color.White,
    primaryContainer = Slate800,
    onPrimaryContainer = BrandIndigoLight,
    secondary = BrandPurple,
    onSecondary = Color.White,
    secondaryContainer = Slate700,
    onSecondaryContainer = BrandPurpleLight,
    tertiary = BrandPink,
    background = Slate900,
    onBackground = Color.White,
    surface = Slate800,
    onSurface = Color.White,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate200,
    outline = Slate600
)

private val LightColorScheme = lightColorScheme(
    primary = BrandIndigoPrimary,
    onPrimary = Color.White,
    primaryContainer = BrandIndigoLight,
    onPrimaryContainer = BrandIndigoPrimary,
    secondary = BrandPurpleDark,
    onSecondary = Color.White,
    secondaryContainer = BrandPurpleLight,
    onSecondaryContainer = BrandPurpleDark,
    tertiary = BrandPink,
    background = Slate50,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    outline = Slate200
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

