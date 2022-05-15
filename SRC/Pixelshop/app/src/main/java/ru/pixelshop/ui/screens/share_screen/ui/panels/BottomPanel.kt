package ru.pixelshop.ui.screens.share_screen.ui.panels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.pixelshop.R
import ru.pixelshop.ui.customs.IconWithShadow
import ru.pixelshop.ui.customs.TextWithShadow

@Composable
fun BottomPanel(
    homeClick: () -> Unit,
    text: String,
    textClick: () -> Unit,
    shareClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(75.dp).copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize
        ),
        color = MaterialTheme.colors.secondaryVariant
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 45.dp)
        ) {
            IconButton(onClick = homeClick, modifier = Modifier.size(45.dp)) {
                IconWithShadow(
                    painter = painterResource(id = R.drawable.ic_home),
                    description = "home",
                    mainTint = MaterialTheme.colors.primary,
                    offsetX = 1.dp,
                    offsetY = 1.dp,
                    alpha = 0.1f
                )
            }
            TextWithShadow(
                text = text,
                style = MaterialTheme.typography.h2,
                letterSpacing = 10.sp,
                offsetX = 4.dp,
                offsetY = 4.dp,
                alpha = 0.25f,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.clickable(onClick = textClick)
            )

            IconButton(onClick = shareClick, modifier = Modifier.size(45.dp)) {
                IconWithShadow(
                    painter = painterResource(id = R.drawable.ic_share),
                    description = "share",
                    mainTint = MaterialTheme.colors.primary,
                    offsetX = 1.dp,
                    offsetY = 1.dp,
                    alpha = 0.1f
                )
            }
        }

    }
}