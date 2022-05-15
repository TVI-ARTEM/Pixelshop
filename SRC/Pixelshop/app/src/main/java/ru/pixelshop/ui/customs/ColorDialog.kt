package ru.pixelshop.ui.customs

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import ru.pixelshop.R
import ru.pixelshop.utils.ColorUtilities

@Composable
fun ColorDialog(
    initColor: Color,
    onDismiss: () -> Unit,
    onSave: (Color) -> Unit
) {
    val red = remember {
        mutableStateOf((255 * initColor.red + 0.5).toInt())
    }
    val green = remember {
        mutableStateOf((255 * initColor.green + 0.5).toInt())
    }
    val blue = remember {
        mutableStateOf((255 * initColor.blue + 0.5).toInt())
    }
    val hex = remember {
        mutableStateOf(String.format("%02X%02X%02X", red.value, green.value, blue.value))
    }
    Dialog(onDismissRequest = {
        onSave(Color(red.value, green.value, blue.value))
        onDismiss()
    }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Surface(
                modifier = Modifier
                    .size(width = 280.dp, height = 400.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 75.dp,
                            topEnd = 75.dp,
                            bottomEnd = 50.dp,
                            bottomStart = 50.dp
                        )
                    ),
                color = MaterialTheme.colors.primary
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 25.dp)
                        .padding(top = 10.dp, bottom = 50.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colors.secondary,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        color = Color(red.value, green.value, blue.value)
                    ) {

                    }

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextWithShadow(
                                text = stringResource(R.string.hex_name),
                                style = MaterialTheme.typography.body1.copy(fontSize = 20.sp),
                                alpha = 0.1f,
                                offsetX = 2.dp,
                                offsetY = 2.dp,
                                color = MaterialTheme.colors.secondary
                            )

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(
                                        shape = RoundedCornerShape(
                                            topStart = 20.dp,
                                            topEnd = 40.dp,
                                            bottomEnd = 20.dp,
                                            bottomStart = 40.dp
                                        )
                                    )
                                    .border(
                                        width = 3.dp,
                                        color = MaterialTheme.colors.secondary,
                                        shape = RoundedCornerShape(
                                            topStart = 20.dp,
                                            topEnd = 40.dp,
                                            bottomEnd = 20.dp,
                                            bottomStart = 40.dp
                                        )
                                    ),
                                textStyle = MaterialTheme.typography.body1.copy(
                                    color = MaterialTheme.colors.secondary,
                                    textAlign = TextAlign.Center
                                ),
                                value = hex.value,
                                onValueChange = {
                                    try {
                                        if (it.length <= 6) {
                                            hex.value = it
                                            val color = Color("#${it}".toColorInt())
                                            red.value = (255 * color.red + 0.5).toInt()
                                            green.value = (255 * color.green + 0.5).toInt()
                                            blue.value = (255 * color.blue + 0.5).toInt()
                                        }
                                    } catch (ignored: Exception) {

                                    }
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = MaterialTheme.colors.primary,
                                    cursorColor = MaterialTheme.colors.secondary,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    disabledTextColor = Color.Transparent,
                                    textColor = MaterialTheme.colors.secondary
                                ),
                                singleLine = true,
                                placeholder = {
                                    TextWithShadow(
                                        text = stringResource(R.string.hex_placeholder),
                                        style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                                        alpha = 0.1f,
                                        offsetX = 2.dp,
                                        offsetY = 2.dp,
                                        color = MaterialTheme.colors.secondaryVariant
                                    )
                                }
                            )

                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        ColorSlider(
                            name = "R",
                            value = red,
                            onValueChange = {
                                hex.value = String.format(
                                    "%02X%02X%02X",
                                    red.value,
                                    green.value,
                                    blue.value
                                )
                            },
                            getColor = Color(red = red.value, green = 0, blue = 0),
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ColorSlider(
                            name = "G",
                            value = green,
                            onValueChange = {
                                hex.value = String.format(
                                    "%02X%02X%02X",
                                    red.value,
                                    green.value,
                                    blue.value
                                )
                            },
                            getColor = Color(green = green.value, red = 0, blue = 0),
                            color = Color.Green
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        ColorSlider(
                            name = "B",
                            value = blue,
                            onValueChange = {
                                hex.value = String.format(
                                    "%02X%02X%02X",
                                    red.value,
                                    green.value,
                                    blue.value
                                )
                            },
                            getColor = Color(blue = blue.value, red = 0, green = 0),
                            color = Color.Blue
                        )
                    }

                    var colors = ColorUtilities.getColors()
                    colors =
                        colors.take(if (colors.size % 2 == 0 || colors.isEmpty()) colors.size else colors.size - 1)
                    val content = emptyList<Pair<Color, Color>>().toMutableList()
                    for (index in 0 until (colors.size / 2)) {
                        content.add(Pair(colors[index], colors[colors.size / 2 + index]))
                    }
                    LazyRow(content = {
                        items(content) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                ColorTemplate(color = it.first, onClick = {
                                    red.value = (it.first.red * 255).toInt()
                                    green.value = (it.first.green * 255).toInt()
                                    blue.value = (it.first.blue * 255).toInt()
                                    hex.value = String.format(
                                        "%02X%02X%02X",
                                        red.value,
                                        green.value,
                                        blue.value
                                    )
                                })

                                Spacer(modifier = Modifier.height(5.dp))
                                ColorTemplate(color = it.second, onClick = {
                                    red.value = (it.second.red * 255).toInt()
                                    green.value = (it.second.green * 255).toInt()
                                    blue.value = (it.second.blue * 255).toInt()
                                    hex.value = String.format(
                                        "%02X%02X%02X",
                                        red.value,
                                        green.value,
                                        blue.value
                                    )
                                })
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                        }

                    }, contentPadding = PaddingValues(start = 5.dp))
                }
            }
            ButtonNext(
                modifier = Modifier.offset(y = (-30).dp),
                title = stringResource(id = R.string.save_text),
                onClick = {
                    onSave(Color(red.value, green.value, blue.value))
                    onDismiss()
                },
                buttonColor = MaterialTheme.colors.primaryVariant
            )
        }
    }
}

@Composable
private fun ColorTemplate(
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(30.dp)
            .clip(shape = RoundedCornerShape(5.dp))
            .border(
                width = (1.5).dp,
                color = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(5.dp)
            )
            .clickable(onClick = onClick),
        color = color
    ) {

    }
}

@Composable
private fun ColorSlider(
    name: String,
    value: MutableState<Int>,
    onValueChange: () -> Unit,
    getColor: Color,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextWithShadow(
            text = name,
            style = MaterialTheme.typography.body1.copy(fontSize = 20.sp),
            alpha = 0.1f,
            offsetX = 2.dp,
            offsetY = 2.dp,
            color = color
        )
        Spacer(modifier = Modifier.width(5.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(30.dp)
        ) {
            Slider(
                value = value.value.toFloat(),
                onValueChange = {
                    value.value = it.toInt()
                    onValueChange()
                },
                valueRange = 0f..255f,
                steps = 244,
                colors = SliderDefaults.colors(
                    thumbColor = getColor,
                    activeTrackColor = getColor,
                    inactiveTrackColor = getColor,
                    inactiveTickColor = getColor,
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
                    text = value.value.toString(),
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