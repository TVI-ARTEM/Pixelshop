package ru.pixelshop.ui.screens.draw_screen.ui.panels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import ru.pixelshop.R
import ru.pixelshop.ui.customs.IconWithShadow
import ru.pixelshop.ui.screens.draw_screen.logic.state.DrawViewScreenState
import ru.pixelshop.ui.screens.draw_screen.logic.state.DrawViewState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomAnimationPanel(
    state: DrawViewState,
    screenState: DrawViewScreenState,
    onPressed: (Int) -> Unit,
    onClick: (Int) -> Unit,
    onAdd: () -> Unit,
    onPlay: () -> Unit
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
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(end = 50.dp)

        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 100.dp,
                            topEnd = 20.dp,
                            bottomEnd = 20.dp
                        )
                    ),
                color = MaterialTheme.colors.primaryVariant
            ) {
                LazyRow(
                    content = {
                        items(state.project.framesCount) { index ->
                            Surface(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .combinedClickable(
                                        onClick = { onClick(index) },
                                        onLongClick = { onPressed(index) }
                                    ),
                                color = if (index == screenState.frameId) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.primary
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Box(modifier = Modifier.size(25.dp)) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_picture),
                                            contentDescription = "frame",
                                            tint = if (index == screenState.frameId) MaterialTheme.colors.primary else MaterialTheme.colors.secondaryVariant
                                        )
                                    }
                                }

                            }
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    },
                    contentPadding = PaddingValues(start = 30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = onAdd, modifier = Modifier.size(42.dp)) {
                    IconWithShadow(
                        painter = painterResource(id = R.drawable.ic_add_anim),
                        description = "add frame",
                        mainTint = MaterialTheme.colors.primary,
                        offsetX = 1.dp,
                        offsetY = 1.dp,
                        alpha = 0.1f
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(onClick = onPlay, modifier = Modifier.size(42.dp)) {
                    IconWithShadow(
                        painter = painterResource(
                            id = if (screenState.gifShowing)
                                R.drawable.ic_pause
                            else
                                R.drawable.ic_anim_play_btn
                        ),
                        description = "play animation",
                        mainTint = MaterialTheme.colors.primary,
                        offsetX = 1.dp,
                        offsetY = 1.dp,
                        alpha = 0.1f
                    )
                }
            }
        }
    }
}