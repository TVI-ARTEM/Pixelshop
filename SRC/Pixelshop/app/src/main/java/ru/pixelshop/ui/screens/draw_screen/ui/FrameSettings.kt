package ru.pixelshop.ui.screens.draw_screen.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.pixelshop.R
import ru.pixelshop.ui.customs.ButtonNext
import ru.pixelshop.ui.customs.TextWithShadow
import ru.pixelshop.utils.Utilities.Companion.format

@Composable
fun FrameSettings(
    bitmap: Bitmap,
    onDismiss: () -> Unit,
    speed: Double,
    position: Int,
    onSpeedChange: (Float) -> Unit,
    onPositionChange: (Int) -> Unit,
    onRemove: () -> Unit,
    positionRange: ClosedFloatingPointRange<Float>,
    positionSteps: Int,
    enable: Boolean
) {
    val speedState = remember {
        mutableStateOf(speed.toFloat())
    }
    val positionState = remember {
        mutableStateOf(position.toFloat())
    }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .size(width = 350.dp, height = 475.dp),
                shape = RoundedCornerShape(100.dp),
                color = MaterialTheme.colors.primary
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(vertical = 50.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(225.dp)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "frame",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(shape = RoundedCornerShape(20.dp)).border(
                                    width = 5.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    color = MaterialTheme.colors.secondaryVariant
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    ) {
                        ScalingPanel(
                            name = stringResource(R.string.speed_text),
                            text = speedState.value.toDouble().format(1),
                            range = 0.1f..10f,
                            steps = 98,

                            value = speedState
                        )
                        if (enable) {
                            ScalingPanel(
                                name = stringResource(R.string.position_text),
                                text = (positionState.value.toInt() + 1).toString(),
                                range = positionRange,
                                steps = positionSteps,

                                value = positionState
                            )
                            IconButton(
                                modifier = Modifier.size(40.dp),
                                onClick = onRemove,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_remove),
                                    contentDescription = "remove",
                                    tint = MaterialTheme.colors.secondaryVariant
                                )
                            }
                        }
                    }
                }
            }
            ButtonNext(
                Modifier.offset(y = (-30).dp),
                title = stringResource(id = R.string.save_text),
                onClick = {
                    onDismiss()
                    onSpeedChange(speedState.value)
                    onPositionChange(positionState.value.toInt())
                },
                buttonColor = MaterialTheme.colors.secondary
            )
        }
    }
}


@Composable
fun ScalingPanel(
    name: String,
    text: String,
    value: MutableState<Float>,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextWithShadow(
            text = name,
            style = MaterialTheme.typography.body1.copy(fontSize = 20.sp),
            alpha = 0.1f,
            offsetX = 2.dp,
            offsetY = 2.dp,
            color = MaterialTheme.colors.secondary
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Slider(
                value = value.value,
                onValueChange = {
                    value.value = it
                },
                valueRange = range,
                steps = steps,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colors.secondaryVariant,
                    activeTrackColor = MaterialTheme.colors.secondaryVariant,
                    inactiveTrackColor = MaterialTheme.colors.secondaryVariant,
                    inactiveTickColor = MaterialTheme.colors.secondaryVariant,
                    activeTickColor = Color.Transparent
                )
            )
        }

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
                    text = text,
                    style = MaterialTheme.typography.body1,
                    alpha = 0.1f,
                    offsetX = 2.dp,
                    offsetY = 2.dp,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}