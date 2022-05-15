package ru.pixelshop.ui.screens.share_screen.ui

import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.pixelshop.R
import ru.pixelshop.ui.customs.Splash
import ru.pixelshop.ui.screens.Screen
import ru.pixelshop.ui.screens.share_screen.logic.event.ShareViewEvent
import ru.pixelshop.ui.screens.share_screen.logic.model.ShareViewModel
import ru.pixelshop.ui.screens.share_screen.ui.panels.AnimationPanel
import ru.pixelshop.ui.screens.share_screen.ui.panels.BottomPanel
import ru.pixelshop.ui.screens.share_screen.ui.panels.ScalingPanel
import ru.pixelshop.utils.ScreenUtilities

@Composable
fun ShareScreen(
    currentScreen: MutableState<Screen>,
    previousScreen: MutableState<Screen>,
    navController: NavController,
    viewModel: ShareViewModel = hiltViewModel()
) {
    ScreenUtilities.LockScreenOrientation(
        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    )

    val onBack: () -> Unit = {
        val tempScreen = previousScreen.value
        previousScreen.value = currentScreen.value
        currentScreen.value = tempScreen
        navController.navigateUp()
    }
    BackHandler(enabled = true, onBack = onBack)

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ShareViewModel.UIEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val gifExport = remember {
        mutableStateOf(false)
    }
    val state = viewModel.state.value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(100.dp).copy(
                topStart = ZeroCornerSize,
                topEnd = ZeroCornerSize
            ),
            color = MaterialTheme.colors.secondaryVariant
        ) {
            if (!gifExport.value) {
                AnimationPanel(state = state, viewModel = viewModel)
            }
        }

        Surface(color = Color.Transparent, modifier = Modifier.weight(1f)) {

        }

        ShowDrawable(gifExport = gifExport, state = state)

        Spacer(modifier = Modifier.height(10.dp))

        ScalingPanel(scaleText = state.scale.toString(), onChangeScale = {
            viewModel.onEvent(ShareViewEvent.ChangeScale(it))
        })


        Surface(color = Color.Transparent, modifier = Modifier.weight(1f)) {

        }

        BottomPanel(
            homeClick = onBack,
            text = if (gifExport.value) "GIF" else "PNG",
            shareClick = {
                if (gifExport.value) {
                    viewModel.onEvent(ShareViewEvent.ExportGIF)
                } else {
                    viewModel.onEvent(ShareViewEvent.ExportPNG)
                }
            }, textClick = {
                gifExport.value = !gifExport.value
            })
    }

    if (state.gifCreating) {
        Splash(painter = painterResource(id = R.drawable.generating))
    }
}

