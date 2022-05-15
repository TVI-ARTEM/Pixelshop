package ru.pixelshop.ui.screens.draw_screen.ui.panels

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.github.patxibocos.matriz.Aspect
import io.github.patxibocos.matriz.GridCanvas
import io.github.patxibocos.matriz.Sizing
import ru.pixelshop.ui.screens.draw_screen.logic.state.DrawViewScreenState
import ru.pixelshop.ui.screens.draw_screen.logic.additional.Tool


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GridPanel(
    screenState: DrawViewScreenState,
    onCellClick: (Offset, Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(
                width = (4 * screenState.scaling * 1.0 / screenState.matrix.size).dp,
                color = if (screenState.border) MaterialTheme.colors.secondary else Color.Transparent
            ),
        color = Color.Transparent
    ) {
        if (!screenState.gifShowing) {

            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            val border = MaterialTheme.colors.secondary

            Row(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .horizontalScroll(
                        rememberScrollState()
                    )
                    .fillMaxSize()
            ) {
                GridCanvas(
                    sizing = Sizing.RowsAndColumns(
                        columns = screenState.width,
                        rows = screenState.height,
                        aspect = Aspect.CellsRatio(1f)
                    ), onDrawCell = { row, column, cellSize ->
                        drawRect(
                            color = if (row % 2 == 0 && column % 2 == 0 ||
                                row % 2 != 0 && column % 2 != 0
                            ) Color.White
                            else Color.Gray,
                            size = cellSize
                        )
                        drawRect(
                            color = Color(screenState.matrix[row][column]),
                            size = cellSize
                        )
                        drawRect(
                            color = Color(screenState.matrix[row][column]),
                            size = cellSize,
                            style = Stroke(
                                width = (16 * screenState.scaling * 1.1 / screenState.matrix.size).dp.toPx(),
                            )
                        )
                        drawRect(
                            color = if (screenState.border) border else Color.Transparent,
                            size = cellSize,
                            style = Stroke(
                                width = (16 * screenState.scaling * 1.1 / screenState.matrix.size).dp.toPx(),
                            )
                        )
                    },
                    modifier = Modifier
                        .size(screenWidth * screenState.scaling)
                        .then(
                            Modifier.canvasGestures(
                                screenState.tool != Tool.MOVE,
                                onCellClick
                            )
                        )
                )
            }

        } else {
            Image(
                painter = rememberDrawablePainter(screenState.gif),
                contentDescription = "gif",
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit
            )
            screenState.gif.start()
        }
    }

}

fun Modifier.canvasGestures(enabled: Boolean, onCellClick: (Offset, Int) -> Unit) =
    if (enabled)
        pointerInput(Unit) {
            detectDragGestures(
                onDrag = { point, _ ->
                    onCellClick(point.position, size.width)
                }
            )
        }.pointerInput(Unit) {
            detectTapGestures(
                onTap = { tapOffset ->
                    onCellClick(tapOffset, size.width)
                }
            )
        }
    else
        Modifier