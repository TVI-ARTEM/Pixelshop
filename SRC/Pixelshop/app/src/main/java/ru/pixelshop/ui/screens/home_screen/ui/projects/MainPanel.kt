package ru.pixelshop.ui.screens.home_screen.ui.projects

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.pixelshop.R
import ru.pixelshop.entities.MatrixSize
import ru.pixelshop.ui.customs.IconWithShadow
import ru.pixelshop.ui.customs.TextWithShadow
import ru.pixelshop.ui.screens.home_screen.logic.model.HomeViewModel
import ru.pixelshop.ui.screens.home_screen.logic.state.HomeViewState
import ru.pixelshop.utils.BitmapUtilities
import ru.pixelshop.utils.Utilities.Companion.parseDate


@Composable
fun MainPanel(
    onUpdate: (Long, String) -> Unit,
    onRemove: (Long) -> Unit,
    onShare: () -> Unit,
    viewModel: HomeViewModel,
    state: HomeViewState,
    itemId: Long,
) {
    val editExpanded = remember {
        mutableStateOf(false)
    }
    val project = state.projects[itemId]!!
    val matrix = state.matrix[itemId]!!
    Box {
        Surface(
            modifier = Modifier
                .width(280.dp)
                .height(400.dp),
            shape = RoundedCornerShape(75.dp),
            color = MaterialTheme.colors.secondaryVariant
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(20.dp))
                Surface(
                    modifier = Modifier
                        .width(240.dp)
                        .height(240.dp),
                    shape = RoundedCornerShape(
                        topStart = 75.dp,
                        topEnd = 75.dp,
                        bottomStart = 50.dp,
                        bottomEnd = 50.dp
                    ),
                    color = MaterialTheme.colors.primary
                ) {
                    Image(
                        bitmap = BitmapUtilities.createBitmap(
                            matrix,
                            project.width,
                            project.height,
                            10
                        )
                            .asImageBitmap(),
                        contentDescription = "first frame",
                        modifier = Modifier.padding(horizontal = 40.dp, vertical = 40.dp),
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Column {
                        TextWithShadow(
                            text = "${stringResource(R.string.size)}: ${
                                MatrixSize(
                                    state.projects[itemId]!!.width,
                                    state.projects[itemId]!!.height
                                )
                            }",
                            style = MaterialTheme.typography.body1,
                            alpha = 0.15f,
                            offsetY = 2.dp,
                            color = MaterialTheme.colors.primary
                        )
                        TextWithShadow(
                            text = state.projects[itemId]!!.lastModified.parseDate(),
                            style = MaterialTheme.typography.body1,
                            alpha = 0.15f,
                            offsetY = 2.dp,
                            color = MaterialTheme.colors.primary
                        )
                    }

                    Box {
                        IconButton(
                            onClick = { editExpanded.value = !editExpanded.value },
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconWithShadow(
                                painter = painterResource(id = R.drawable.ic_edit),
                                description = "edit button",
                                mainTint = MaterialTheme.colors.primary,
                                offsetX = 2.dp,
                                offsetY = 2.dp,
                                alpha = 0.25f
                            )
                        }
                    }
                }
            }
        }
    }
    ItemSettings(
        onUpdate = onUpdate, onRemove = onRemove,
        onShare = onShare, viewModel = viewModel,
        state = state, itemId = itemId, expanded = editExpanded
    )
}



