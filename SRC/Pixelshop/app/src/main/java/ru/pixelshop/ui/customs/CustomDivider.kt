package ru.pixelshop.ui.customs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun CustomDivider(tint: Color, height: Dp) {
    Spacer(modifier = Modifier.height(height))
    Divider(color = tint)
    Spacer(modifier = Modifier.height(height))
}