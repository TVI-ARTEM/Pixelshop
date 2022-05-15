package ru.pixelshop.ui.customs

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import ru.pixelshop.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomDropDownMenu(
    modifier: Modifier = Modifier,
    expanded: MutableState<Boolean>,
    selectedOption: MutableState<String>,
    options: List<String>,
    onClick: (Int, String) -> Unit,
    textColor: Color,
    colorItem: Color,
    dropdownOffset: DpOffset,
) {
    val icon = painterResource(
        id = if (expanded.value)
            R.drawable.ic_arrow_up
        else
            R.drawable.ic_arrow_down
    )

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Column() {
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
                )
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            textStyle = MaterialTheme.typography.body1.copy(color = textColor),
            value = selectedOption.value,
            onValueChange = {
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
            readOnly = true,
            trailingIcon = {
                Icon(icon,
                    contentDescription = "drop arrow",
                    tint = textColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { expanded.value = !expanded.value })
            }
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = modifier
                .width(
                    with(LocalDensity.current) {
                        textFieldSize.width.toDp()
                    }
                ),
            offset = dropdownOffset,
        ) {

            options.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption.value = selectionOption
                        expanded.value = false
                        onClick(index, selectionOption)
                    }
                ) {
                    TextWithShadow(
                        text = selectionOption,
                        style = MaterialTheme.typography.body1,
                        alpha = 0.1f,
                        offsetY = 2.dp,
                        offsetX = 2.dp,
                        color = colorItem,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }
}