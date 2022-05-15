package ru.pixelshop.ui.screens.home_screen.ui

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.pixelshop.R
import ru.pixelshop.ui.customs.Splash
import ru.pixelshop.ui.screens.Screen
import ru.pixelshop.ui.screens.home_screen.logic.event.HomeProjectEvent
import ru.pixelshop.ui.screens.home_screen.logic.model.HomeViewModel
import ru.pixelshop.ui.screens.home_screen.ui.panels.BottomPanel
import ru.pixelshop.ui.screens.home_screen.ui.panels.ProjectPanel
import ru.pixelshop.ui.screens.home_screen.ui.panels.TopPanel
import ru.pixelshop.utils.ScreenUtilities

@Composable
fun HomeScreen(
    currentScreen: MutableState<Screen>,
    previousScreen: MutableState<Screen>,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    ScreenUtilities.LockScreenOrientation(
        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    )

    val state = viewModel.state.value
    val projectState = viewModel.projectState.value
    val isDialogOpen = remember { mutableStateOf(false) }


    Surface(color = Color.Transparent, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            TopPanel()
            ProjectPanel(
                currentScreen = currentScreen,
                previousScreen = previousScreen,
                navController = navController,
                viewModel = viewModel,
                state = state,
                onAdd = { isDialogOpen.value = true },
                onUpdate = { index, item ->
                    viewModel.onEvent(HomeProjectEvent.UpdateTitle(index, item))
                },
                onRemove = {
                    viewModel.onEvent(HomeProjectEvent.DeleteProject(it))
                }
            )
            BottomPanel(
                offsetX = 3.dp,
                offsetY = 3.dp,
                alpha = 0.15f,
                onLanguageClick = {}
            ) { isDialogOpen.value = true }
        }
    }

    AddProjectDialog(
        isDialogOpen = isDialogOpen,
        viewModel = viewModel,
        state = projectState,
        templates = state.templatesMatrix
    )

    if (currentScreen.value != Screen.HOME_SCREEN) {
        Splash(painter = painterResource(id = R.drawable.loading))
    }
}

