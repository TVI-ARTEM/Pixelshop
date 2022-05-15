package ru.pixelshop.ui.screens.home_screen.ui.panels

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.pixelshop.R
import ru.pixelshop.ui.customs.IconWithShadow

@Composable
fun BottomPanel(
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    alpha: Float = 1f,
    onLanguageClick: () -> Unit,
    onAddClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(75.dp).copy(
                bottomStart = ZeroCornerSize,
                bottomEnd = ZeroCornerSize
            ), color = MaterialTheme.colors.secondary
        ) {

        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 60.dp, end = 60.dp)
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = onLanguageClick,
                modifier = Modifier
                    .size(60.dp)
                    .clip(shape = CircleShape)
                    .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_russian_lang),
                    contentDescription = "language button",
                )
            }

            IconButton(
                onClick = onAddClick,
                modifier = Modifier
                    .size(80.dp)
                    .offset(
                        y = (-15).dp
                    ),
            ) {
                IconWithShadow(
                    painter = painterResource(id = R.drawable.ic_add_btn),
                    description = "add button",
                    mainTint = MaterialTheme.colors.primaryVariant,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    alpha = alpha
                )
            }

            Box(modifier = Modifier.size(60.dp))
        }
    }
}