package ru.pixelshop.ui.customs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.pixelshop.ui.theme.AnotherBlue

@Composable
fun Splash(painter: Painter) {
    Dialog(onDismissRequest = {
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier
                    .size(height = 300.dp, width = 300.dp)
                    .clip(shape = RoundedCornerShape(50.dp)),
                color = AnotherBlue
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(50.dp))
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(painter = painter, contentDescription = "splash image", modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}