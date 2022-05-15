package ru.pixelshop.ui.screens.draw_screen.ui.panels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.pixelshop.R
import ru.pixelshop.ui.customs.IconWithShadow

@Composable
fun ZoomPanel(
    ZoomChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(60.dp)
                .clip(shape = CircleShape)
                .clickable { ZoomChanged(1) }, color = MaterialTheme.colors.secondaryVariant
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(42.dp)) {
                    IconWithShadow(
                        painter = painterResource(id = R.drawable.ic_zoom_in),
                        description = "zoom in",
                        mainTint = MaterialTheme.colors.primary,
                        offsetX = 1.dp,
                        offsetY = 1.dp,
                        alpha = 0.1f
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Surface(
            modifier = Modifier
                .size(60.dp)
                .clip(shape = CircleShape)
                .clickable { ZoomChanged(-1) }, color = MaterialTheme.colors.secondaryVariant
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(42.dp)) {
                    IconWithShadow(
                        painter = painterResource(id = R.drawable.ic_zoom_out),
                        description = "zoom out",
                        mainTint = MaterialTheme.colors.primary,
                        offsetX = 1.dp,
                        offsetY = 1.dp,
                        alpha = 0.1f
                    )
                }
            }


        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}