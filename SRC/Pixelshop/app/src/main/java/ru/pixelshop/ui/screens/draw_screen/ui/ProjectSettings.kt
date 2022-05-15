package ru.pixelshop.ui.screens.draw_screen.ui

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.flow.collectLatest
import ru.pixelshop.R
import ru.pixelshop.ui.customs.ButtonNext
import ru.pixelshop.ui.customs.CustomDivider
import ru.pixelshop.ui.customs.IconWithShadow
import ru.pixelshop.ui.customs.TextWithShadow
import ru.pixelshop.ui.screens.draw_screen.logic.model.DrawViewModel
import ru.pixelshop.ui.screens.draw_screen.logic.state.DrawViewScreenState
import ru.pixelshop.ui.screens.draw_screen.logic.state.DrawViewState
import ru.pixelshop.ui.theme.Red

@Composable
fun ProjectSettings(
    onUpdate: (String) -> Unit,
    onShare: () -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onExit: () -> Unit,
    viewModel: DrawViewModel,
    state: DrawViewState,
    screenState: DrawViewScreenState,
) {
    val isRenameDialogOpen = remember {
        mutableStateOf(false)
    }
    if (screenState.settingsOpen) {

        Dialog(
            onDismissRequest = onDismiss
        ) {
            Surface(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .width(190.dp)
                    .clip(
                        shape = RoundedCornerShape(25.dp)
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDismiss()
                                onSave()
                            },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(modifier = Modifier.size(20.dp), color = Color.Transparent) {
                            IconWithShadow(
                                painter = painterResource(id = R.drawable.ic_save),
                                description = "share",
                                mainTint = MaterialTheme.colors.secondary,
                                offsetX = 2.dp,
                                offsetY = 2.dp,
                                alpha = 0.1f
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        TextWithShadow(
                            text = stringResource(id = R.string.save_text),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.secondary
                        )
                    }

                    CustomDivider(tint = MaterialTheme.colors.secondary, height = 10.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDismiss()
                                onShare()
                            },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(modifier = Modifier.size(20.dp), color = Color.Transparent) {
                            IconWithShadow(
                                painter = painterResource(id = R.drawable.ic_share),
                                description = "share",
                                mainTint = MaterialTheme.colors.secondary,
                                offsetX = 2.dp,
                                offsetY = 2.dp,
                                alpha = 0.1f
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        TextWithShadow(
                            text = stringResource(id = R.string.share_text),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.secondary
                        )
                    }

                    CustomDivider(tint = MaterialTheme.colors.secondary, height = 10.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDismiss()
                                isRenameDialogOpen.value = true
                            },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(modifier = Modifier.size(20.dp), color = Color.Transparent) {
                            IconWithShadow(
                                painter = painterResource(id = R.drawable.ic_rename),
                                description = "rename",
                                mainTint = MaterialTheme.colors.secondary,
                                offsetX = 2.dp,
                                offsetY = 2.dp,
                                alpha = 0.1f
                            )
                        }

                        Spacer(modifier = Modifier.width(15.dp))
                        TextWithShadow(
                            text = stringResource(id = R.string.rename_text),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.secondary
                        )
                    }

                    CustomDivider(tint = MaterialTheme.colors.secondary, height = 10.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDismiss()
                                onExit()
                            },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(modifier = Modifier.size(20.dp), color = Color.Transparent) {
                            IconWithShadow(
                                painter = painterResource(id = R.drawable.ic_home),
                                description = "exit",
                                mainTint = MaterialTheme.colors.secondary,
                                offsetX = 2.dp,
                                offsetY = 2.dp,
                                alpha = 0.1f
                            )
                        }

                        Spacer(modifier = Modifier.width(15.dp))
                        TextWithShadow(
                            text = stringResource(R.string.exit_text),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
            }
        }
    }

    if (isRenameDialogOpen.value) {
        UpdateTitle(
            onDismiss = { isRenameDialogOpen.value = false },
            onUpdate = onUpdate,
            viewModel = viewModel,
            state = state,
        )
    }

}

@Composable
fun UpdateTitle(
    onDismiss: () -> Unit,
    onUpdate: (String) -> Unit,
    viewModel: DrawViewModel,
    state: DrawViewState,
) {
    val context = LocalContext.current
    val title = remember {
        mutableStateOf(state.projectEntity.name)
    }

    val initialize: () -> Unit = {
        title.value = state.projectEntity.name
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is DrawViewModel.UIEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                is DrawViewModel.UIEvent.UpdateProject -> {
                    initialize()
                    onDismiss()
                }
            }
        }
    }

    Dialog(
        onDismissRequest = {
            initialize()
            onDismiss()
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(width = 280.dp, height = 180.dp),
                shape = RoundedCornerShape(75.dp), color = MaterialTheme.colors.primary
            ) {
                Column(Modifier.padding(top = 40.dp)) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        TextWithShadow(
                            text = stringResource(id = R.string.title),
                            style = MaterialTheme.typography.body1,
                            alpha = 0.1f,
                            offsetY = 2.dp,
                            offsetX = 2.dp,
                            color = MaterialTheme.colors.secondary,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        TextField(
                            modifier = Modifier
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
                            placeholder = {
                                TextWithShadow(
                                    text = stringResource(R.string.hint_title_project),
                                    style = MaterialTheme.typography.body2.copy(fontSize = 12.sp),
                                    alpha = 0.1f,
                                    offsetY = 2.dp,
                                    offsetX = 2.dp,
                                    color = MaterialTheme.colors.secondaryVariant,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondary),
                            value = title.value,
                            onValueChange = {
                                title.value = it.replace("\n", "")
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.primary,
                            ),
                            singleLine = true
                        )
                    }
                }
            }

            ButtonNext(
                modifier = Modifier.offset(y = (-30).dp),
                title = stringResource(R.string.save_text),
                onClick = {
                    onUpdate(title.value)
                },
                buttonColor = Red
            )
        }
    }
}