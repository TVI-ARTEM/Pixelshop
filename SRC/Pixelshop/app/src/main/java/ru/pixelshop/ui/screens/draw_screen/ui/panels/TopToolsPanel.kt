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

@Composable
fun TopToolsPanel(
    UndoEnabled: Boolean,
    RedoEnabled: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onTool: () -> Unit,
    onPalette: () -> Unit,
    onAnimation: () -> Unit,
    onGame: () -> Unit,
    onEdit: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(30.dp).copy(
            topStart = ZeroCornerSize,
            topEnd = ZeroCornerSize
        ),
        color = MaterialTheme.colors.secondaryVariant
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 15.dp)
        ) {
            Row {
                IconButton(
                    onClick = onUndo,
                    modifier = Modifier.size(30.dp),
                    enabled = UndoEnabled
                ) {
                    IconWithShadow(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        description = "undo",
                        mainTint = MaterialTheme.colors.primary,
                        offsetX = 1.dp,
                        offsetY = 1.dp,
                        alpha = 0.1f
                    )
                }

                IconButton(
                    onClick = onRedo,
                    modifier = Modifier.size(30.dp),
                    enabled = RedoEnabled
                ) {
                    IconWithShadow(
                        painter = painterResource(id = R.drawable.ic_arrow_forward),
                        description = "redo",
                        mainTint = MaterialTheme.colors.primary,
                        offsetX = 1.dp,
                        offsetY = 1.dp,
                        alpha = 0.1f
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = onTool, modifier = Modifier.size(42.dp)) {
                    IconWithShadow(
                        painter = painterResource(id = R.drawable.ic_tools),
                        description = "tools",
                        mainTint = MaterialTheme.colors.primary,
                        offsetX = 1.dp,
                        offsetY = 1.dp,
                        alpha = 0.1f
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(onClick = onPalette, modifier = Modifier.size(42.dp)) {
                    IconWithShadow(
                        painter = painterResource(id = R.drawable.ic_palette),
                        description = "palette",
                        mainTint = MaterialTheme.colors.primary,
                        offsetX = 1.dp,
                        offsetY = 1.dp,
                        alpha = 0.1f
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(onClick = onAnimation, modifier = Modifier.size(42.dp)) {
                    IconWithShadow(
                        painter = painterResource(id = R.drawable.ic_animation),
                        description = "animation",
                        mainTint = MaterialTheme.colors.primary,
                        offsetX = 1.dp,
                        offsetY = 1.dp,
                        alpha = 0.1f
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(onClick = onGame, modifier = Modifier.size(42.dp)) {
                    IconWithShadow(
                        painter = painterResource(id = R.drawable.conway_logo),
                        description = "conway's life",
                        mainTint = MaterialTheme.colors.primary,
                        offsetX = 1.dp,
                        offsetY = 1.dp,
                        alpha = 0.1f
                    )
                }

            }

            IconButton(onClick = onEdit, modifier = Modifier.size(24.dp)) {
                IconWithShadow(
                    painter = painterResource(id = R.drawable.ic_edit),
                    description = "edit",
                    mainTint = MaterialTheme.colors.primary,
                    offsetX = 1.dp,
                    offsetY = 1.dp,
                    alpha = 0.1f
                )
            }
        }
    }
}