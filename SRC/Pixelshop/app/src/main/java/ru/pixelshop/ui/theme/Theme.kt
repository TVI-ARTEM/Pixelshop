package ru.pixelshop.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


private val LightColorPalette = lightColors(
    primary = White,
    secondary = DarkGray,
    primaryVariant = Red,
    secondaryVariant = Gray,
    background = BackgroundColor,
    surface = Blue,
)

@Composable
fun PixelshopTheme(content: @Composable () -> Unit) {
    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}