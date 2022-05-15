package ru.pixelshop.ui.screens.home_screen.ui.projects

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.pixelshop.ui.customs.ButtonNext
import ru.pixelshop.ui.screens.Screen
import ru.pixelshop.ui.screens.home_screen.logic.model.HomeViewModel
import ru.pixelshop.ui.screens.home_screen.logic.state.HomeViewState

@Composable
fun ProjectItem(
    currentScreen: MutableState<Screen>,
    previousScreen: MutableState<Screen>,
    navController: NavController,
    onUpdate: (Long, String) -> Unit,
    onRemove: (Long) -> Unit,
    viewModel: HomeViewModel,
    state: HomeViewState,
    itemId: Long,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 15.dp)
    ) {
        MainPanel(
            onUpdate = onUpdate,
            onRemove = onRemove,
            onShare = {
                previousScreen.value = currentScreen.value
                currentScreen.value = Screen.SHARE_SCREEN
                navController.navigate(
                    Screen.SHARE_SCREEN.route +
                            "?projectId=${itemId}"
                )
            },
            viewModel = viewModel,
            state = state,
            itemId = itemId
        )
        ButtonNext(
            Modifier.offset(y = (-50).dp),
            title = state.projects[itemId]!!.name,
            onClick = {
                previousScreen.value = currentScreen.value
                currentScreen.value = Screen.DRAW_SCREEN
                navController.navigate(
                    Screen.DRAW_SCREEN.route +
                            "?projectId=${itemId}"
                )
            },
            buttonColor = MaterialTheme.colors.secondary
        )
    }
}




