package ru.pixelshop.ui.screens.home_screen.logic.model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.pixelshop.database.data.entities.ProjectEntity
import ru.pixelshop.database.data.exceptions.InvalidProjectException
import ru.pixelshop.database.data.use_case.ProjectUseCases
import ru.pixelshop.entities.MatrixSize
import ru.pixelshop.entities.Project
import ru.pixelshop.ui.screens.home_screen.logic.event.HomeProjectEvent
import ru.pixelshop.ui.screens.home_screen.logic.state.HomeViewProjectState
import ru.pixelshop.ui.screens.home_screen.logic.state.HomeViewState
import ru.pixelshop.utils.BitmapUtilities
import ru.pixelshop.utils.BitmapUtilities.Companion.toBase64
import ru.pixelshop.utils.BitmapUtilities.Companion.toBitmap
import ru.pixelshop.utils.BitmapUtilities.Companion.toMatrix
import ru.pixelshop.utils.ProjectUtilities.Companion.toFrameContainer
import ru.pixelshop.utils.ProjectUtilities.Companion.toJson
import ru.pixelshop.utils.Utilities.Companion.toMatrixSize
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: ProjectUseCases
) : ViewModel() {
    private val _state = mutableStateOf(HomeViewState())
    val state: State<HomeViewState> = _state

    private val _projectState = mutableStateOf(HomeViewProjectState())
    val projectState: State<HomeViewProjectState> = _projectState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        useCases.getProjects().onEach { items ->
            val matrices: List<List<List<Int>>> = items.map {
                it.preview.toBitmap().toMatrix()
            }
            _state.value = state.value.copy(
                projects = items.map { it.id to it }.toMap(),
                matrix = matrices.mapIndexed { index, list -> items[index].id to list }.toMap()
            )
        }.launchIn(viewModelScope)

        useCases.getTemplates().onEach { items ->
            val matrices: List<List<List<Int>>> = items.map {
                it.matrix.toBitmap().toMatrix()
            }
            _state.value = state.value.copy(
                templatesMatrix = matrices.mapIndexed { index, list -> items[index].name to list }
                    .toMap()
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeProjectEvent) {
        try {
            when (event) {
                is HomeProjectEvent.CreateProject -> onCreateProject()
                is HomeProjectEvent.CreateProjectTemplate -> onCreateProjectTemplate(event.value)
                is HomeProjectEvent.ChangedTitle -> onChangeTitle(event.value)
                is HomeProjectEvent.ChangedSize -> onChangeSize(event.value.toMatrixSize())
                is HomeProjectEvent.UpdateTitle -> onUpdateTitle(event.id, event.value)
                is HomeProjectEvent.DeleteProject -> onDeleteProject(event.value)
            }
        } catch (exception: Exception) {
            Log.i("Exception", exception.message ?: "Something went wrong")
        }
    }

    private fun onCreateProject() {
        viewModelScope.launch {
            try {
                val project = Project(projectState.value.size.width, projectState.value.size.height)
                val projectEntity = ProjectEntity(
                    name = projectState.value.title, width = project.width,
                    height = project.height,
                    preview = BitmapUtilities.createBitmap(
                        matrix = project.getFrame(0).matrix,
                        width = project.width, height = project.height, scale = 1
                    ).toBase64(),
                    frames = project.toFrameContainer().toJson(),
                    lastModified = LocalDateTime.now().toString()
                )
                useCases.addProject(projectEntity)
                _eventFlow.emit(UIEvent.CreateProject(id = projectEntity.id))

            } catch (exception: InvalidProjectException) {
                _eventFlow.emit(
                    UIEvent.ShowMessage(
                        message = exception.message ?: "Couldn't save project"
                    )
                )
            }
        }
    }

    private fun onCreateProjectTemplate(name: String) {
        viewModelScope.launch {
            try {
                val matrix = state.value.templatesMatrix[name]!!
                val height = matrix.size
                val width = matrix[0].size
                val project = Project(height = height, width = width)
                for (row in matrix.indices) {
                    for (column in matrix[row].indices) {
                        project.getFrame(0).update(
                            row = row,
                            column = column,
                            colorInt = matrix[row][column]
                        )
                    }
                }

                val projectEntity = ProjectEntity(
                    name = name,
                    width = width,
                    height = height,
                    preview = BitmapUtilities.createBitmap(
                        matrix = project.getFrame(0).matrix,
                        width = project.width,
                        height = project.height,
                        scale = 1
                    ).toBase64(),
                    frames = project.toFrameContainer().toJson(),
                    lastModified = LocalDateTime.now().toString()
                )
                useCases.addProject(projectEntity)
                _eventFlow.emit(UIEvent.CreateProject(id = projectEntity.id))

            } catch (exception: InvalidProjectException) {
                _eventFlow.emit(
                    UIEvent.ShowMessage(
                        message = exception.message ?: "Couldn't save project"
                    )
                )
            }
        }
    }

    private fun onChangeTitle(title: String) {
        _projectState.value = projectState.value.copy(
            title = title
        )
    }

    private fun onChangeSize(size: MatrixSize) {
        _projectState.value = projectState.value.copy(
            size = size
        )
    }

    private fun onUpdateTitle(id: Long, title: String) {
        viewModelScope.launch {
            try {
                useCases.updateProject(
                    state.value.projects[id]!!.copy(
                        name = title,
                        lastModified = LocalDateTime.now().toString()
                    )
                )
                _eventFlow.emit(UIEvent.UpdateProject)

            } catch (exception: InvalidProjectException) {
                _eventFlow.emit(
                    UIEvent.ShowMessage(
                        message = exception.message ?: "Couldn't save project"
                    )
                )
            }
        }
    }

    private fun onDeleteProject(id: Long) {
        viewModelScope.launch {
            useCases.deleteProjectById(id)
        }
    }


    sealed class UIEvent {
        data class ShowMessage(val message: String) : UIEvent()
        data class CreateProject(val id: Long) : UIEvent()
        object UpdateProject : UIEvent()
    }
}
