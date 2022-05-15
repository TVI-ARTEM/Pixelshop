package ru.pixelshop.ui.screens.share_screen.ui.panels

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.pixelshop.R
import ru.pixelshop.ui.customs.TextWithShadow

@Composable
fun ScalingPanel(
    scaleText: String,
    onChangeScale: (Int) -> Unit
) {
    val scaling = remember {
        mutableStateOf(0f)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextWithShadow(
                text = stringResource(R.string.scaling_text),
                style = MaterialTheme.typography.body1.copy(fontSize = 20.sp),
                alpha = 0.1f,
                offsetX = 2.dp,
                offsetY = 2.dp,
                color = MaterialTheme.colors.secondary
            )

            Spacer(modifier = Modifier.width(5.dp))

            Surface(
                modifier = Modifier
                    .size(width = 40.dp, height = 30.dp)
                    .clip(shape = RoundedCornerShape(5.dp))
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.secondary,
                        shape = RoundedCornerShape(5.dp)
                    ),
                color = MaterialTheme.colors.primary
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextWithShadow(
                        text = scaleText,
                        style = MaterialTheme.typography.body1,
                        alpha = 0.1f,
                        offsetX = 2.dp,
                        offsetY = 2.dp,
                        color = MaterialTheme.colors.secondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Slider(
            value = scaling.value,
            onValueChange = {
                scaling.value = it
                onChangeScale((it * 100 + 1).toInt())
            },
            valueRange = 0f..0.2f,
            steps = 18,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.secondaryVariant,
                activeTrackColor = MaterialTheme.colors.secondaryVariant,
                inactiveTrackColor = MaterialTheme.colors.secondaryVariant,
                inactiveTickColor = MaterialTheme.colors.secondaryVariant,
                activeTickColor = Color.Transparent
            )
        )

    }
}