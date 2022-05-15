package ru.pixelshop.ui.customs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextWithShadow(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    letterSpacing: TextUnit = 0.sp,
    color: Color = Color.White,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    alpha: Float = 1f,
) {
    Box {
        Text(
            text = text,
            color = color,
            modifier = modifier
                .offset(
                    x = offsetX,
                    y = offsetY
                )
                .alpha(alpha),
            style = style,
            letterSpacing = letterSpacing
        )
        Text(
            text = text,
            style = style,
            letterSpacing = letterSpacing,
            color = color,
            modifier = modifier
        )
    }
}