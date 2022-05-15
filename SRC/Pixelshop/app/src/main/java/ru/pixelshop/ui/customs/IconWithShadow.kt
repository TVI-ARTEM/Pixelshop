package ru.pixelshop.ui.customs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconWithShadow(
    painter: Painter,
    description: String,
    mainTint: Color,
    offsetX: Dp = 5.dp,
    offsetY: Dp = 5.dp,
    alpha: Float = 1f
) {
    Box {
        Icon(
            painter = painter,
            contentDescription = description,
            tint = mainTint,
            modifier = Modifier
                .offset(x = offsetX, y = offsetY)
                .alpha(alpha)
        )
        Icon(
            painter = painter,
            contentDescription = description,
            tint = mainTint
        )
    }
}