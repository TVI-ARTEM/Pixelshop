package ru.pixelshop.ui.screens.share_screen.ui.panels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.pixelshop.R
import ru.pixelshop.ui.screens.share_screen.logic.event.ShareViewEvent
import ru.pixelshop.ui.screens.share_screen.logic.model.ShareViewModel
import ru.pixelshop.ui.screens.share_screen.logic.state.ShareViewState

@Composable
fun AnimationPanel(
    state: ShareViewState,
    viewModel: ShareViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(horizontal = 30.dp)

    ) {
        LazyRow(
            content = {
                items(state.frames.size) { index ->
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .clickable(
                                onClick = {
                                    viewModel.onEvent(
                                        ShareViewEvent.ChangeFrame(
                                            index
                                        )
                                    )
                                },
                            ),
                        color = MaterialTheme.colors.primary
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
                                    tint = if (index == state.frameId) MaterialTheme.colors.surface else MaterialTheme.colors.secondaryVariant
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            },
            contentPadding = PaddingValues(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        )
    }
}