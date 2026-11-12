package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EthioLightColorScheme = lightColorScheme(
    primary = EthioGreen,
    onPrimary = OnEthioGreen,
    primaryContainer = EthioGreenContainer,
    onPrimaryContainer = OnEthioGreen,
    secondary = EthioGold,
    secondaryContainer = EthioGoldContainer,
    onSecondaryContainer = OnEthioGoldContainer,
    tertiary = EthioRed,
    onTertiary = OnEthioRed,
    tertiaryContainer = EthioRedContainer,
    onTertiaryContainer = OnEthioRed,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceContainerHighest,
    onSurfaceVariant = OnSurfaceVariantLight,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    background = SurfaceLight,
    onBackground = OnSurfaceLight,
    error = EthioRed,
    onError = OnEthioRed
)

private val EthioDarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnSurfaceLight,
    primaryContainer = EthioGreen,
    onPrimaryContainer = OnEthioGreen,
    secondary = SecondaryDark,
    secondaryContainer = EthioGold,
    tertiary = TertiaryDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceContainerDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    surfaceContainerLowest = SurfaceDark,
    surfaceContainerLow = SurfaceContainerDark,
    surfaceContainer = SurfaceContainerHighDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighDark,
    background = SurfaceDark,
    onBackground = OnSurfaceDark,
    error = EthioRed,
    onError = OnEthioRed
)

@Composable
fun ETBVerifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) EthioDarkColorScheme else EthioLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun CounterWatchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    ETBVerifyTheme(darkTheme = darkTheme, content = content)
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    ETBVerifyTheme(darkTheme = darkTheme, content = content)
}
