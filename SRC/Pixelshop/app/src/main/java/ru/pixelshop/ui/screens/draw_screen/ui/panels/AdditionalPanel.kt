package ru.pixelshop.ui.screens.draw_screen.ui.panels

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import ru.pixelshop.ui.customs.IconWithShadow
import ru.pixelshop.ui.screens.draw_screen.logic.state.DrawViewScreenState
import ru.pixelshop.ui.screens.draw_screen.logic.additional.Mirror

@Composable
fun AdditionalPanel(
    color: Color,
    onGridChanged: () -> Unit,
    onColorClick: () -> Unit,
    onMirrorClick: () -> Unit,
    screenState: DrawViewScreenState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape)
                .clickable { onMirrorClick() }, color = MaterialTheme.colors.secondaryVariant
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(36.dp)) {
                    IconWithShadow(
                        painter = painterResource(
                            id = when (screenState.mirror) {
                                Mirror.OFF -> R.drawable.ic_mirror_off
                                Mirror.X -> R.drawable.ic_mirror_x
                                Mirror.Y -> R.drawable.ic_mirror_y
                                Mirror.FULL -> R.drawable.ic_mirror_full
                            }
                        ),
                        description = "mirror change",
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
                .size(50.dp)
                .clip(shape = CircleShape)
                .border(
                    shape = CircleShape,
                    width = 2.dp,
                    color = MaterialTheme.colors.secondaryVariant
                )
                .clickable { onColorClick() },
            color = color
        ) {

        }
        Spacer(modifier = Modifier.width(10.dp))
        Surface(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape)
                .clickable { onGridChanged() }, color = MaterialTheme.colors.secondaryVariant
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(36.dp)) {
                    IconWithShadow(
                        painter = painterResource(id = R.drawable.ic_turn_grid),
                        description = "grid changed",
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