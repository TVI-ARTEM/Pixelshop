package ru.pixelshop.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import ru.pixelshop.ui.screens.draw_screen.ui.DrawScreen
import ru.pixelshop.ui.screens.home_screen.ui.HomeScreen
import ru.pixelshop.ui.screens.share_screen.ui.ShareScreen
import ru.pixelshop.ui.theme.PixelshopTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PixelshopTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    val currentScreen = remember {
                        mutableStateOf(Screen.HOME_SCREEN)
                    }
                    val previousScreen = remember {
                        mutableStateOf(Screen.HOME_SCREEN)
                    }
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HOME_SCREEN.route
                    ) {
                        composable(route = Screen.HOME_SCREEN.route) {
                            HomeScreen(currentScreen = currentScreen,previousScreen = previousScreen, navController = navController)
                        }
                        composable(
                            route = Screen.DRAW_SCREEN.route +
                                    "?projectId={projectId}",
                            arguments = listOf(
                                navArgument(
                                    name = "projectId"
                                ) {
                                    type = NavType.LongType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            DrawScreen(currentScreen = currentScreen,previousScreen = previousScreen, navController = navController)
                        }
                        composable(
                            route = Screen.SHARE_SCREEN.route +
                                    "?projectId={projectId}",
                            arguments = listOf(
                                navArgument(
                                    name = "projectId"
                                ) {
                                    type = NavType.LongType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            ShareScreen(currentScreen = currentScreen,previousScreen = previousScreen,navController = navController)
                        }
                    }
                }
            }
        }
    }
}
