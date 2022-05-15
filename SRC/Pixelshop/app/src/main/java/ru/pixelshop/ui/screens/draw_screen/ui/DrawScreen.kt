package ru.pixelshop.ui.screens.draw_screen.ui

import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ru.pixelshop.R
import ru.pixelshop.ui.customs.ColorDialog
import ru.pixelshop.ui.customs.Splash
import ru.pixelshop.ui.screens.Screen
import ru.pixelshop.ui.screens.draw_screen.logic.event.DrawViewEvent
import ru.pixelshop.ui.screens.draw_screen.logic.model.DrawViewModel
import ru.pixelshop.ui.screens.draw_screen.ui.panels.*
import ru.pixelshop.utils.BitmapUtilities
import ru.pixelshop.utils.ScreenUtilities
import kotlin.math.max
import kotlin.time.Duration.Companion.seconds

@Composable
fun DrawScreen(
    currentScreen: MutableState<Screen>,
    previousScreen: MutableState<Screen>,
    navController: NavController,
    viewModel: DrawViewModel = hiltViewModel()
) {
    ScreenUtilities.LockScreenOrientation(
        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    )

    val state = viewModel.state.value
    val screenState = viewModel.screenState.value
    val onBack: () -> Unit = {
        viewModel.onEvent(DrawViewEvent.SaveProject)
        previousScreen.value = currentScreen.value
        currentScreen.value = Screen.HOME_SCREEN
        navController.navigateUp()
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(10.seconds)
            viewModel.onEvent(DrawViewEvent.SaveProject)
        }
    }

    BackHandler(enabled = true, onBack = onBack)
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        TopToolsPanel(
            onUndo = {
                viewModel.onEvent(DrawViewEvent.Undo)
                viewModel.onEvent(DrawViewEvent.StopPlayingGif)
                viewModel.onEvent(DrawViewEvent.AlgorithmStop)
            },
            onRedo = {
                viewModel.onEvent(DrawViewEvent.Redo)
                viewModel.onEvent(DrawViewEvent.StopPlayingGif)
                viewModel.onEvent(DrawViewEvent.AlgorithmStop)
            },
            onTool = {
                viewModel.onEvent(DrawViewEvent.ChangeToolVisible)
            },
            onPalette = {
                viewModel.onEvent(DrawViewEvent.ChangeColorDialogVisible)
            },
            onAnimation = {
                viewModel.onEvent(DrawViewEvent.ChangeAnimationVisible)
            },
            onGame = {
                viewModel.onEvent(DrawViewEvent.ChangeAlgorithmVisible)
            },
            onEdit = {
                viewModel.onEvent(DrawViewEvent.ChangeSettingsVisible)
            },
            UndoEnabled = !screenState.history.isUndoEmpty,
            RedoEnabled = !screenState.history.isRedoEmpty,
        )
        Spacer(modifier = Modifier.height(15.dp))
        AdditionalPanel(
            color = screenState.drawColor,
            onGridChanged = {
                if (!screenState.gifShowing && !screenState.algorithmExecuting) {
                    viewModel.onEvent(DrawViewEvent.ChangeGridVisible)
                }
            },
            onColorClick = { viewModel.onEvent(DrawViewEvent.ChangeColorDialogVisible) },
            onMirrorClick = {
                if (!screenState.gifShowing && !screenState.algorithmExecuting) {
                    viewModel.onEvent(DrawViewEvent.ChangeMirror)
                }
            },
            screenState = screenState
        )
        Spacer(modifier = Modifier.height(15.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            color = Color.Transparent
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                GridPanel(screenState = screenState) { offset, width ->
                    viewModel.onEvent(DrawViewEvent.AlgorithmStop)
                    viewModel.onEvent(DrawViewEvent.StopPlayingGif)
                    viewModel.onEvent(DrawViewEvent.OnCells(offset, width))
                }
            }

        }
        Spacer(modifier = Modifier.height(15.dp))

        ZoomPanel(ZoomChanged = {
            if (!screenState.gifShowing && !screenState.algorithmExecuting) {
                viewModel.onEvent(DrawViewEvent.ChangeZoom(it))
            }
        })
        Surface(
            color = Color.Transparent, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
        }

        if (screenState.toolsVisible) {
            BottomToolsPanel(
                onToolTap = { viewModel.onEvent(DrawViewEvent.ChangeTool(it)) },
                isToolSelected = { it == screenState.tool }
            )
        }
        if (screenState.animationVisible) {
            BottomAnimationPanel(
                state = state,
                screenState = screenState,
                onPressed = {
                    viewModel.onEvent(DrawViewEvent.StopPlayingGif)
                    viewModel.onEvent(DrawViewEvent.AlgorithmStop)
                    viewModel.onEvent(DrawViewEvent.SelectFrame(it))
                    viewModel.onEvent(DrawViewEvent.ChangeFrameSettingsVisible)
                },
                onClick = {
                    viewModel.onEvent(DrawViewEvent.StopPlayingGif)
                    viewModel.onEvent(DrawViewEvent.AlgorithmStop)
                    viewModel.onEvent(DrawViewEvent.SelectFrame(it))
                },
                onAdd = {
                    viewModel.onEvent(DrawViewEvent.StopPlayingGif)
                    viewModel.onEvent(DrawViewEvent.AlgorithmStop)
                    viewModel.onEvent(DrawViewEvent.AddFrame)
                },
                onPlay = {
                    if (screenState.gifShowing)
                        viewModel.onEvent(DrawViewEvent.StopPlayingGif)
                    else {
                        viewModel.onEvent(DrawViewEvent.AlgorithmStop)
                        viewModel.onEvent(DrawViewEvent.ShowGif)
                    }
                }
            )
        }

        if (screenState.algorithmVisible) {
            BottomAlgorithmPanel(
                screenState = screenState,
                onPlay = {
                    if (screenState.algorithmExecuting) {
                        viewModel.onEvent(DrawViewEvent.AlgorithmStop)
                        viewModel.onEvent(DrawViewEvent.SaveProject)
                    } else {
                        viewModel.onEvent(DrawViewEvent.StopPlayingGif)
                        viewModel.onEvent(DrawViewEvent.AlgorithmStart)
                        viewModel.onEvent(DrawViewEvent.AlgorithmExecute)
                    }
                }
            )
        }

    }

    if (screenState.colorDialogVisible) {
        ColorDialog(
            initColor = screenState.drawColor,
            onDismiss = { viewModel.onEvent(DrawViewEvent.ChangeColorDialogVisible) },
            onSave = { viewModel.onEvent(DrawViewEvent.ChangeColor(it)) })
    }

    if (screenState.gifCreating) {
        Splash(painter = painterResource(id = R.drawable.generating))
    }

    if (currentScreen.value != Screen.DRAW_SCREEN) {
        Splash(painter = painterResource(id = R.drawable.loading))
    }

    ProjectSettings(
        onUpdate = { viewModel.onEvent(DrawViewEvent.UpdateTitle(it)) },
        onShare = {
            previousScreen.value = currentScreen.value
            currentScreen.value = Screen.SHARE_SCREEN
            viewModel.onEvent(DrawViewEvent.SaveProject)
            navController.navigate(
                Screen.SHARE_SCREEN.route +
                        "?projectId=${state.projectEntity.id}"
            )
        },
        onDismiss = { viewModel.onEvent(DrawViewEvent.ChangeSettingsVisible) },
        onSave = { viewModel.onEvent(DrawViewEvent.SaveProject) },
        onExit = onBack,
        viewModel = viewModel,
        state = state,
        screenState = screenState
    )

    if (screenState.frameSettingsOpen) {
        FrameSettings(
            bitmap = BitmapUtilities.createBitmap(
                matrix = state.project.getFrame(screenState.frameId).matrix,
                width = screenState.width,
                height = screenState.height,
                scale = 10
            ),
            onDismiss = { viewModel.onEvent(DrawViewEvent.ChangeFrameSettingsVisible) },
            onRemove = {
                viewModel.onEvent(DrawViewEvent.ChangeFrameSettingsVisible)
                viewModel.onEvent(DrawViewEvent.RemoveFrame)
            },
            onPositionChange = { viewModel.onEvent(DrawViewEvent.ChangePosition(it)) },
            onSpeedChange = { viewModel.onEvent(DrawViewEvent.ChangeSpeed(it.toDouble())) },
            speed = state.project.getFrame(screenState.frameId).speed,
            position = screenState.frameId,
            positionRange = 0f..(state.project.framesCount - 1).toFloat(),
            positionSteps = max(0, state.project.framesCount - 2),
            enable = state.project.framesCount > 1
        )
    }

}