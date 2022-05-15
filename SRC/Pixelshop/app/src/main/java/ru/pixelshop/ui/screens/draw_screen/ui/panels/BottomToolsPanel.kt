package ru.pixelshop.ui.screens.draw_screen.ui.panels

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
import ru.pixelshop.R
import ru.pixelshop.ui.customs.IconWithShadow
import ru.pixelshop.ui.screens.draw_screen.logic.additional.Tool

@Composable
fun BottomToolsPanel(
    onToolTap: (Tool) -> Unit,
    isToolSelected: (Tool) -> Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(100.dp).copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize
        ),
        color = MaterialTheme.colors.secondaryVariant
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 50.dp)
        ) {
            IconButton(onClick = { onToolTap(Tool.PENCIL) }, modifier = Modifier.size(42.dp)) {
                IconWithShadow(
                    painter = painterResource(id = R.drawable.ic_pencil),
                    description = "pencil",
                    mainTint = if (isToolSelected(Tool.PENCIL)) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary,
                    offsetX = 1.dp,
                    offsetY = 1.dp,
                    alpha = 0.1f
                )
            }

            IconButton(onClick = { onToolTap(Tool.PIPETTE) }, modifier = Modifier.size(42.dp)) {
                IconWithShadow(
                    painter = painterResource(id = R.drawable.ic_pipette),
                    description = "pipette",
                    mainTint = if (isToolSelected(Tool.PIPETTE)) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary,
                    offsetX = 1.dp,
                    offsetY = 1.dp,
                    alpha = 0.1f
                )
            }

            IconButton(onClick = { onToolTap(Tool.ERASER) }, modifier = Modifier.size(42.dp)) {
                IconWithShadow(
                    painter = painterResource(id = R.drawable.ic_eraser),
                    description = "eraser",
                    mainTint = if (isToolSelected(Tool.ERASER)) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary,
                    offsetX = 1.dp,
                    offsetY = 1.dp,
                    alpha = 0.1f
                )
            }

            IconButton(onClick = { onToolTap(Tool.FILLER) }, modifier = Modifier.size(42.dp)) {
                IconWithShadow(
                    painter = painterResource(id = R.drawable.ic_filler),
                    description = "filler",
                    mainTint = if (isToolSelected(Tool.FILLER)) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary,
                    offsetX = 1.dp,
                    offsetY = 1.dp,
                    alpha = 0.1f
                )
            }

            IconButton(onClick = { onToolTap(Tool.MOVE) }, modifier = Modifier.size(42.dp)) {
                IconWithShadow(
                    painter = painterResource(id = R.drawable.ic_move_hand),
                    description = "move",
                    mainTint = if (isToolSelected(Tool.MOVE)) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary,
                    offsetX = 1.dp,
                    offsetY = 1.dp,
                    alpha = 0.1f
                )
            }

        }
    }
}