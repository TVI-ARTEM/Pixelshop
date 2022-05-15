package ru.pixelshop.ui.screens.home_screen.ui

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.flow.collectLatest
import ru.pixelshop.R
import ru.pixelshop.entities.MatrixSize
import ru.pixelshop.ui.customs.ButtonNext
import ru.pixelshop.ui.customs.CustomDropDownMenu
import ru.pixelshop.ui.customs.TextWithShadow
import ru.pixelshop.ui.screens.home_screen.logic.event.HomeProjectEvent
import ru.pixelshop.ui.screens.home_screen.logic.model.HomeViewModel
import ru.pixelshop.ui.screens.home_screen.logic.state.HomeViewProjectState
import ru.pixelshop.ui.theme.Red
import ru.pixelshop.utils.BitmapUtilities
import ru.pixelshop.utils.Utilities.Companion.pow

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun AddProjectDialog(
    isDialogOpen: MutableState<Boolean>,
    viewModel: HomeViewModel,
    state: HomeViewProjectState,
    templates: Map<String, List<List<Int>>>
) {
    if (isDialogOpen.value) {
        val firstTime = remember {
            mutableStateOf(true)
        }
        val context = LocalContext.current

        val optionsSize = List(5) { MatrixSize(2.pow(it + 3)).toString() }

        val expandedSize = remember { mutableStateOf(false) }

        val selectedOptionSize = remember { mutableStateOf(optionsSize[0]) }

        val templateSelect = remember { mutableStateOf(false) }

        val reInitialize: () -> Unit = {
            selectedOptionSize.value = optionsSize[0]
            viewModel.onEvent(HomeProjectEvent.ChangedTitle(""))
            viewModel.onEvent(HomeProjectEvent.ChangedSize(selectedOptionSize.value))
            templateSelect.value = false
        }

        if (firstTime.value) {
            reInitialize()
            firstTime.value = false
        }

        LaunchedEffect(key1 = true) {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is HomeViewModel.UIEvent.ShowMessage -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                    }
                    is HomeViewModel.UIEvent.CreateProject -> {
                        reInitialize()
                        isDialogOpen.value = false
                    }
                    is HomeViewModel.UIEvent.UpdateProject -> {
                        // IGNORED
                    }
                }
            }
        }

        Dialog(onDismissRequest = {
            reInitialize()
            isDialogOpen.value = false
        }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Surface(
                    modifier = Modifier.size(width = 280.dp, height = 380.dp),
                    shape = RoundedCornerShape(75.dp), color = MaterialTheme.colors.primary
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        if (!templateSelect.value) {
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
                                    value = state.title,
                                    onValueChange = {
                                        viewModel.onEvent(
                                            HomeProjectEvent.ChangedTitle(
                                                it.replace(
                                                    "\n",
                                                    ""
                                                )
                                            )
                                        )
                                    },
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = MaterialTheme.colors.primary,
                                    ),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 30.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                TextWithShadow(
                                    text = stringResource(R.string.size),
                                    style = MaterialTheme.typography.body1,
                                    alpha = 0.1f,
                                    offsetY = 2.dp,
                                    offsetX = 2.dp,
                                    color = MaterialTheme.colors.secondary,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(10.dp))



                                CustomDropDownMenu(
                                    expanded = expandedSize,
                                    selectedOption = selectedOptionSize,
                                    options = optionsSize,
                                    onClick = { _, item ->
                                        viewModel.onEvent(HomeProjectEvent.ChangedSize(item))
                                    },
                                    textColor = MaterialTheme.colors.secondary,
                                    colorItem = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .background(color = MaterialTheme.colors.secondary)
                                        .clip(shape = RoundedCornerShape(50.dp)),
                                    dropdownOffset = DpOffset(x = 0.dp, y = 10.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                        } else {
                            Surface(
                                modifier = Modifier.size(width = 240.dp, height = 250.dp),
                                color = Color.Transparent,
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                LazyVerticalGrid(cells = GridCells.Fixed(2), content = {
                                    items(templates.keys.toList()) { name ->
                                        Card(
                                            backgroundColor = MaterialTheme.colors.secondary,
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .fillMaxWidth(),
                                            elevation = 8.dp,
                                            shape = RoundedCornerShape(10.dp),
                                            onClick = {
                                                viewModel.onEvent(
                                                    HomeProjectEvent.CreateProjectTemplate(
                                                        name
                                                    )
                                                )
                                            }
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                val matrix = templates[name]!!

                                                Card(
                                                    backgroundColor = MaterialTheme.colors.primary,
                                                    modifier = Modifier
                                                        .padding(10.dp)
                                                        .fillMaxWidth()
                                                        .aspectRatio(1f),
                                                    elevation = 8.dp,
                                                    shape = RoundedCornerShape(10.dp)
                                                ) {
                                                    Image(
                                                        bitmap = BitmapUtilities.createBitmap(
                                                            matrix = matrix,
                                                            width = matrix[0].size,
                                                            height = matrix.size, 10
                                                        ).asImageBitmap(),
                                                        contentDescription = "template image",
                                                        modifier = Modifier.padding(10.dp)
                                                    )
                                                }

                                                TextWithShadow(
                                                    text = name,
                                                    style = MaterialTheme.typography.body1,
                                                    alpha = 0.1f,
                                                    offsetY = 2.dp,
                                                    offsetX = 2.dp,
                                                    color = MaterialTheme.colors.primary
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                            }
                                        }
                                    }
                                })
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                        }


                        Box {
                            TextWithShadow(
                                text = stringResource(
                                    id = if (!templateSelect.value) R.string.template_creation
                                    else R.string.custom_creation
                                ),
                                style = MaterialTheme.typography.body1.copy(textDecoration = TextDecoration.Underline),
                                alpha = 0.1f,
                                offsetY = 2.dp,
                                offsetX = 2.dp,
                                color = MaterialTheme.colors.secondary,
                                modifier = Modifier
                                    .clickable { templateSelect.value = !templateSelect.value }
                            )
                        }

                    }
                }

                if (!templateSelect.value) {
                    ButtonNext(
                        modifier = Modifier.offset(y = (-30).dp),
                        title = stringResource(R.string.create_btn),
                        onClick = {
                            viewModel.onEvent(HomeProjectEvent.CreateProject)
                        },
                        buttonColor = Red
                    )
                }
            }
        }
    }
}

