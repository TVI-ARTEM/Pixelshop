package ru.pixelshop.ui.customs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.pixelshop.R

@Composable
fun ButtonNext(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    buttonColor: Color
) {
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .height(60.dp)
                .width(240.dp)
                .clip(
                    shape = RoundedCornerShape(50.dp)
                )
                .clickable(onClick = onClick),
            color = buttonColor
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 30.dp)
            ) {
                TextWithShadow(
                    text = title,
                    style = MaterialTheme.typography.body1,
                    alpha = 0.1f,
                    offsetX = 2.dp,
                    offsetY = 2.dp
                )

                Surface(
                    color = Color.Transparent,
                    modifier = Modifier.width(32.dp)
                ) {
                    IconWithShadow(
                        painter = painterResource(id = R.drawable.ic_arrow_forward_save),
                        description = "arrow forward",
                        mainTint = Color.White,
                        alpha = 0.1f,
                        offsetX = 2.dp,
                        offsetY = 2.dp
                    )
                }
            }
        }
    }
}