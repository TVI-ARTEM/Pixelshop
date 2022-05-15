package ru.pixelshop.ui.screens.home_screen.ui.panels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.pixelshop.R
import ru.pixelshop.ui.customs.IconWithShadow
import ru.pixelshop.ui.customs.TextWithShadow
import ru.pixelshop.ui.screens.Screen
import ru.pixelshop.ui.screens.home_screen.logic.model.HomeViewModel
import ru.pixelshop.ui.screens.home_screen.logic.state.HomeViewState
import ru.pixelshop.ui.screens.home_screen.ui.projects.ProjectItem

@Composable
fun ProjectPanel(
    currentScreen: MutableState<Screen>,
    previousScreen: MutableState<Screen>,
    navController: NavController,
    viewModel: HomeViewModel,
    state: HomeViewState,
    onAdd: () -> Unit,
    onUpdate: (Long, String) -> Unit,
    onRemove: (Long) -> Unit
) {
    Column {
        Row(modifier = Modifier.padding(start = 20.dp)) {
            TextWithShadow(
                text = stringResource(R.string.my_projects),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondaryVariant,
                offsetY = 1.dp,
                offsetX = 1.dp,
                alpha = 0.1f
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        LazyRow(
            content = {
                if (state.projects.isNotEmpty()) {
                    items(state.projects.keys.toList()) { item ->
                        ProjectItem(
                            currentScreen = currentScreen,
                            previousScreen = previousScreen,
                            navController = navController,
                            onUpdate = onUpdate,
                            onRemove = onRemove,
                            viewModel = viewModel,
                            state = state,
                            itemId = item
                        )
                    }
                } else {
                    item {
                        Surface(
                            modifier = Modifier
                                .width(310.dp)
                                .height(400.dp)
                                .padding(horizontal = 15.dp)
                                .clip(
                                    shape = RoundedCornerShape(75.dp)
                                )
                                .clickable(onClick = onAdd),
                            color = MaterialTheme.colors.secondaryVariant
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Surface(
                                    modifier = Modifier.size(240.dp), color = Color.Transparent
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        IconWithShadow(
                                            painter = painterResource(id = R.drawable.ic_add_plus),
                                            description = "add new project",
                                            mainTint = MaterialTheme.colors.primary,
                                            alpha = 0.2f
                                        )
                                    }

                                }
                            }


                        }
                    }
                }

            },
            contentPadding = PaddingValues(horizontal = 15.dp),
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}