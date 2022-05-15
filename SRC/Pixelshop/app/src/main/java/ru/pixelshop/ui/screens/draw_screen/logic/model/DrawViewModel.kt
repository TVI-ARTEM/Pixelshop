package ru.pixelshop.ui.screens.draw_screen.logic.model

import android.graphics.drawable.AnimationDrawable
import android.util.Log
import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.cells.CellInfo
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.pixelshop.PixelshopApp
import ru.pixelshop.database.data.exceptions.InvalidProjectException
import ru.pixelshop.database.data.use_case.ProjectUseCases
import ru.pixelshop.ui.screens.draw_screen.logic.additional.Mirror
import ru.pixelshop.ui.screens.draw_screen.logic.additional.Tool
import ru.pixelshop.ui.screens.draw_screen.logic.event.DrawViewEvent
import ru.pixelshop.ui.screens.draw_screen.logic.additional.Tool.*
import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.Action
import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.cells.CellRecover
import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.frames.*
import ru.pixelshop.ui.screens.draw_screen.logic.state.DrawViewScreenState
import ru.pixelshop.ui.screens.draw_screen.logic.state.DrawViewState
import ru.pixelshop.utils.BitmapUtilities
import ru.pixelshop.utils.BitmapUtilities.Companion.toBase64
import ru.pixelshop.utils.ProjectUtilities
import ru.pixelshop.utils.ProjectUtilities.Companion.toFrameContainer
import ru.pixelshop.utils.ProjectUtilities.Companion.toJson
import ru.pixelshop.utils.ProjectUtilities.Companion.toProject
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DrawViewModel @Inject constructor(
    private val useCases: ProjectUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(DrawViewState())
    val state: State<DrawViewState> = _state

    private val _screenState = mutableStateOf(DrawViewScreenState())
    val screenState: State<DrawViewScreenState> = _screenState

    private var currentProjectId: Long? = null

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Long>("projectId")?.let { projectId ->
            if (projectId != -1L) {
                viewModelScope.launch {
                    useCases.getProject(projectId)?.also { projectEntity ->
                        currentProjectId = projectEntity.id
                        val project = projectEntity.frames.toFrameContainer()
                            .toProject(projectEntity.width, projectEntity.height)
                        _state.value = state.value.copy(
                            project = project,
                            projectEntity = projectEntity,
                            title = projectEntity.name
                        )
                        _screenState.value = screenState.value.copy(
                            matrix = project.getFrame(screenState.value.frameId).matrix.map { it.toMutableList() }
                                .toMutableList(),
                            height = projectEntity.height,
                            width = projectEntity.width
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: DrawViewEvent) {
        try {
            when (event) {
                //region Visible settings
                is DrawViewEvent.ChangeAnimationVisible -> {
                    _screenState.value = screenState.value.copy(
                        animationVisible = !screenState.value.animationVisible,
                        toolsVisible = false,
                        colorDialogVisible = false,
                        settingsOpen = false,
                        algorithmVisible = false
                    )
                }
                is DrawViewEvent.ChangeColorDialogVisible -> {
                    _screenState.value = screenState.value.copy(
                        colorDialogVisible = !screenState.value.colorDialogVisible,
                        settingsOpen = false
                    )
                }
                is DrawViewEvent.ChangeToolVisible -> {
                    _screenState.value = screenState.value.copy(
                        animationVisible = false,
                        toolsVisible = !screenState.value.toolsVisible,
                        colorDialogVisible = false,
                        settingsOpen = false,
                        algorithmVisible = false
                    )
                }
                is DrawViewEvent.ChangeGridVisible -> {
                    _screenState.value = screenState.value.copy(
                        border = !screenState.value.border
                    )
                }
                is DrawViewEvent.ChangeAlgorithmVisible -> {
                    _screenState.value = screenState.value.copy(
                        animationVisible = false,
                        toolsVisible = false,
                        colorDialogVisible = false,
                        settingsOpen = false,
                        algorithmVisible = !screenState.value.algorithmVisible
                    )
                }
                is DrawViewEvent.ChangeSettingsVisible -> {
                    _screenState.value = screenState.value.copy(
                        settingsOpen = !screenState.value.settingsOpen
                    )
                }
                is DrawViewEvent.ChangeFrameSettingsVisible -> {
                    _screenState.value = screenState.value.copy(
                        frameSettingsOpen = !screenState.value.frameSettingsOpen
                    )
                }
                //endregion

                //region Frame settings
                is DrawViewEvent.AddFrame -> onAddFrame()
                is DrawViewEvent.RemoveFrame -> onRemoveFrame()
                is DrawViewEvent.SelectFrame -> onSelectFrame(event.value)
                is DrawViewEvent.ChangePosition -> onChangePosition(event.value)
                is DrawViewEvent.ChangeSpeed -> onChangeSpeed(event.value)
                //endregion

                //region Gif playing
                is DrawViewEvent.ShowGif -> onShowGif()
                is DrawViewEvent.StopPlayingGif -> stopGifPlaying()
                //endregion

                //region Algorithm
                is DrawViewEvent.AlgorithmExecute -> onAlgorithmExecute()
                is DrawViewEvent.AlgorithmStart -> onAlgorithmStart()
                is DrawViewEvent.AlgorithmStop -> onAlgorithmStop()
                //endregion

                //region Draw settings
                is DrawViewEvent.OnCells -> onCells(event.offset, event.width)
                is DrawViewEvent.ChangeTool -> onChangeTool(event.value)
                is DrawViewEvent.ChangeColor -> onChangeColor(event.value)
                is DrawViewEvent.ChangeMirror -> onChangeMirror()
                //endregion

                //region Project settings
                is DrawViewEvent.UpdateTitle -> onUpdateTitle(event.value)
                is DrawViewEvent.SaveProject -> onSaveProject()
                is DrawViewEvent.ChangeZoom -> onChangeZoom(event.value)
                //endregion

                //region History
                is DrawViewEvent.Undo -> onUndo()
                is DrawViewEvent.Redo -> onRedo()
                //endregion
            }
        } catch (exception: Exception) {
            Log.i("Exception", exception.message ?: "Something went wrong")
        }
    }

    //region Frame settings
    private fun onAddFrame() {
        _state.value.project.addFrame(screenState.value.frameId)

        _screenState.value = screenState.value.copy(
            frameId = screenState.value.frameId + 1,
            matrix = state.value.project.getFrame(screenState.value.frameId + 1).matrix.map { it.toMutableList() }
                .toMutableList(),
        )

        val recover = FrameRemoveRecover(
            frameId = screenState.value.frameId,
            onRecover = { frameRecover, actionRecover ->
                onRemoveFrameRecover(
                    frameId = frameRecover,
                    action = actionRecover
                )
            }
        )

        _screenState.value.history.clearRedo()
        _screenState.value.history.addUndo(recover)
    }

    private fun onRemoveFrame() {
        val matrix = state.value.project.getFrame(screenState.value.frameId).matrix
        val recover = FrameRecover(
            frameId = screenState.value.frameId,
            matrix = matrix,
            onRecover = { frameRecover, matrixRecover, actionRecover ->
                onFrameRecover(
                    frameId = frameRecover,
                    matrix = matrixRecover,
                    action = actionRecover
                )
            }
        )

        _state.value.project.remove(screenState.value.frameId)
        _screenState.value = screenState.value.copy(
            frameId = max(
                min(
                    state.value.project.framesCount - 1,
                    screenState.value.frameId
                ), 0
            ),
        )
        _screenState.value = screenState.value.copy(
            matrix = state.value.project.getFrame(screenState.value.frameId).matrix.map { it.toMutableList() }
                .toMutableList(),
        )
        _screenState.value.history.clearRedo()
        _screenState.value.history.addUndo(recover)
    }

    private fun onSelectFrame(frameId: Int) {
        if (frameId == screenState.value.frameId) {
            return
        }
        val recover = FrameSelectRecover(
            frameId = screenState.value.frameId,
            onRecover = { recoverIndex, recoverAction ->
                onRecoverFrameSelect(frameId = recoverIndex, action = recoverAction)
            }
        )

        _screenState.value = screenState.value.copy(
            frameId = frameId,
            matrix = state.value.project.getFrame(frameId).matrix.map { it.toMutableList() }
                .toMutableList(),
        )
        _screenState.value.history.clearRedo()

        _screenState.value.history.addUndo(recover)
    }

    private fun onChangePosition(frameId: Int) {
        if (frameId == screenState.value.frameId) {
            return
        }
        val recover = FramePositionRecover(
            frameId = frameId,
            prevFrameId = screenState.value.frameId,
            onRecover = { frameRecover, prevFrameRecover, actionRecover ->
                onPositionFrameRecover(
                    frameId = frameRecover,
                    prevFrameId = prevFrameRecover,
                    actionRecover
                )
            }
        )

        _state.value.project.replace(screenState.value.frameId, frameId)
        _screenState.value = screenState.value.copy(
            frameId = frameId,
            matrix = state.value.project.getFrame(frameId).matrix.map { it.toMutableList() }
                .toMutableList()
        )
        _screenState.value.history.clearRedo()
        _screenState.value.history.addUndo(recover)
    }

    private fun onChangeSpeed(speed: Double) {
        if (speed == state.value.project.getFrame(screenState.value.frameId).speed) {
            return
        }
        val recover = FrameSpeedRecover(
            frameId = screenState.value.frameId,
            speed = state.value.project.getFrame(screenState.value.frameId).speed,
            onRecover = { frameRecover, speedRecover, actionRecover ->
                onSpeedFrameRecover(
                    frameId = frameRecover,
                    speed = speedRecover,
                    action = actionRecover
                )
            }
        )
        _state.value.project.getFrame(screenState.value.frameId).speed = speed
        _screenState.value.history.clearRedo()
        _screenState.value.history.addUndo(recover)
    }

    private fun onFrameRecover(frameId: Int, matrix: List<List<Int>>, action: Action) {
        val recover = FrameRemoveRecover(
            frameId = frameId,
            onRecover = { frameRecover, actionRecover ->
                onRemoveFrameRecover(
                    frameId = frameRecover,
                    action = actionRecover
                )
            }
        )

        _state.value.project.addFrame(frameId - 1)
        for (row in matrix.indices) {
            for (column in matrix[row].indices) {
                _state.value.project.getFrame(frameId)
                    .update(row = row, column = column, colorInt = matrix[row][column])
            }
        }

        _screenState.value = screenState.value.copy(
            frameId = frameId,
            matrix = state.value.project.getFrame(frameId).matrix.map { it.toMutableList() }
                .toMutableList(),
        )

        if (action == Action.UNDO) {
            _screenState.value.history.addRedo(recover)
        } else {
            _screenState.value.history.addUndo(recover)
        }
    }

    private fun onRemoveFrameRecover(frameId: Int, action: Action) {
        val matrix = state.value.project.getFrame(frameId).matrix
        val recover = FrameRecover(
            frameId = frameId,
            matrix = matrix,
            onRecover = { frameRecover, matrixRecover, actionRecover ->
                onFrameRecover(
                    frameId = frameRecover,
                    matrix = matrixRecover,
                    action = actionRecover
                )
            }
        )

        _state.value.project.remove(frameId)
        _screenState.value = screenState.value.copy(
            frameId = max(min(state.value.project.framesCount - 1, screenState.value.frameId), 0),
        )
        _screenState.value = screenState.value.copy(
            matrix = state.value.project.getFrame(screenState.value.frameId).matrix.map { it.toMutableList() }
                .toMutableList(),
        )

        if (action == Action.UNDO) {
            _screenState.value.history.addRedo(recover)
        } else {
            _screenState.value.history.addUndo(recover)
        }
    }

    private fun onRecoverFrameSelect(frameId: Int, action: Action) {
        val recover = FrameSelectRecover(
            frameId = screenState.value.frameId,
            onRecover = { recoverIndex, recoverAction ->
                onRecoverFrameSelect(frameId = recoverIndex, action = recoverAction)
            }
        )
        _screenState.value = screenState.value.copy(
            frameId = frameId,
            matrix = state.value.project.getFrame(frameId).matrix.map { it.toMutableList() }
                .toMutableList(),
        )

        if (action == Action.UNDO) {
            _screenState.value.history.addRedo(recover)
        } else {
            _screenState.value.history.addUndo(recover)
        }
    }

    private fun onPositionFrameRecover(frameId: Int, prevFrameId: Int, action: Action) {
        val recover = FramePositionRecover(
            frameId = prevFrameId,
            prevFrameId = frameId,
            onRecover = { frameRecover, prevFrameRecover, actionRecover ->
                onPositionFrameRecover(
                    frameId = frameRecover,
                    prevFrameId = prevFrameRecover,
                    actionRecover
                )
            }
        )

        _state.value.project.replace(frameId, prevFrameId)
        _screenState.value = screenState.value.copy(
            frameId = prevFrameId,
            matrix = state.value.project.getFrame(prevFrameId).matrix.map { it.toMutableList() }
                .toMutableList()
        )

        if (action == Action.UNDO) {
            _screenState.value.history.addRedo(recover)
        } else {
            _screenState.value.history.addUndo(recover)
        }
    }

    private fun onSpeedFrameRecover(frameId: Int, speed: Double, action: Action) {
        val recover = FrameSpeedRecover(
            frameId = frameId,
            speed = state.value.project.getFrame(frameId).speed,
            onRecover = { frameRecover, speedRecover, actionRecover ->
                onSpeedFrameRecover(
                    frameId = frameRecover,
                    speed = speedRecover,
                    action = actionRecover
                )
            }
        )
        _state.value.project.getFrame(frameId).speed = speed

        if (action == Action.UNDO) {
            _screenState.value.history.addRedo(recover)
        } else {
            _screenState.value.history.addUndo(recover)
        }
    }
    //endregion

    //region Gif playing
    private fun onShowGif() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _screenState.value = screenState.value.copy(
                    gifCreating = true
                )
                val bitmaps = BitmapUtilities.generateBitmaps(
                    project = state.value.project,
                    scale = abs(100 - state.value.project.width) / 2
                )

                val animation = AnimationDrawable()
                for (item in bitmaps) {
                    animation.addFrame(
                        item.second.toDrawable(PixelshopApp.context.resources),
                        (item.first * 1000).toInt()
                    )
                }
                _screenState.value = screenState.value.copy(
                    gifCreating = false,
                    gifShowing = true,
                    gif = animation
                )
            } catch (exception: Exception) {
                _eventFlow.emit(
                    UIEvent.ShowMessage(
                        message = exception.message ?: "Couldn't save project"
                    )
                )
            }

            _screenState.value = screenState.value.copy(
                gifCreating = false
            )
        }
    }

    private fun stopGifPlaying() {
        _screenState.value = screenState.value.copy(
            gifShowing = false
        )
    }
    //endregion

    //region Algorithm
    private fun onAlgorithmExecute() {
        CoroutineScope(Dispatchers.IO).launch {
            while (screenState.value.algorithmExecuting) {
                _state.value.project.addFrame(screenState.value.frameId)
                _screenState.value = screenState.value.copy(
                    frameId = screenState.value.frameId + 1
                )
                val frame = _state.value.project.getFrame(screenState.value.frameId)
                val matrix = MutableList(state.value.project.height) {
                    MutableList(state.value.project.width) {
                        Color.Transparent.toArgb()
                    }
                }
                for (row in frame.matrix.indices) {
                    for (column in frame.matrix[row].indices) {
                        val color = Color(frame.matrix[row][column])
                        val cellInfo = neighborsCount(position = Pair(row, column), frame.matrix)
                        matrix[row][column] =
                            if (color.alpha == 1f && cellInfo.first == 2 || cellInfo.first == 3)
                                cellInfo.second
                            else
                                Color.Transparent.toArgb()
                    }
                }
                for (row in frame.matrix.indices) {
                    for (column in frame.matrix[row].indices) {
                        frame.update(row = row, column = column, colorInt = matrix[row][column])
                    }
                }
                _screenState.value = screenState.value.copy(
                    matrix = state.value.project.getFrame(screenState.value.frameId).matrix.map { it.toMutableList() }
                        .toMutableList(),
                )

                val recover = FrameRemoveRecover(
                    frameId = screenState.value.frameId,
                    onRecover = { frameRecover, actionRecover ->
                        onRemoveFrameRecover(
                            frameId = frameRecover,
                            action = actionRecover
                        )
                    }
                )

                _screenState.value.history.clearRedo()
                _screenState.value.history.addUndo(recover)
                delay(frame.speed.seconds)
            }
        }
    }

    private fun onAlgorithmStart() {
        _screenState.value = screenState.value.copy(
            algorithmExecuting = true
        )
    }

    private fun onAlgorithmStop() {
        _screenState.value = screenState.value.copy(
            algorithmExecuting = false
        )
    }

    private fun neighborsCount(position: Pair<Int, Int>, matrix: List<List<Int>>): Pair<Int, Int> {
        var count = 0
        val colorMap = mutableMapOf<Int, Int>()
        val directions = listOf(
            Pair(-1, -1),
            Pair(-1, 0),
            Pair(-1, 1),
            Pair(0, -1),
            Pair(0, 1),
            Pair(1, -1),
            Pair(1, 0),
            Pair(1, 1)
        )
        for (direct in directions) {
            println()
            val color = Color(
                matrix.get(((position.first + direct.first) + state.value.project.height) % state.value.project.height)
                    .get(((position.second + direct.second) + state.value.project.width) % state.value.project.width)
            )
            if (color.alpha == 1f) {
                count++
                colorMap += color.toArgb() to (1 + colorMap.getOrDefault(color.toArgb(), 0))
            }
        }
        return Pair(count, colorMap.maxByOrNull { it.value }?.key ?: Color.Transparent.toArgb())
    }
    //endregion

    //region Draw settings
    private fun onCells(offset: Offset, width: Int) {
        val position = ProjectUtilities.getPosition(
            screenState.value.height,
            screenState.value.height,
            offset,
            width
        )
        when (screenState.value.tool) {
            PENCIL -> onDraw(position = position, color = screenState.value.drawColor)
            ERASER -> onDraw(position = position, color = Color.Transparent)
            PIPETTE -> onPipette(position = position)
            FILLER -> onFiller(position = position)
            MOVE -> { /* IGNORED */
            }
        }
    }

    private fun onChangeTool(tool: Tool) {
        _screenState.value = screenState.value.copy(
            tool = tool
        )
    }

    private fun onChangeColor(color: Color) {
        _screenState.value = screenState.value.copy(
            drawColor = color
        )
    }

    private fun onChangeMirror() {
        _screenState.value = screenState.value.copy(
            mirror = screenState.value.mirror.getNext()
        )
    }

    private fun onDraw(position: Pair<Int, Int>, color: Color) {
        val frame = _state.value.project.getFrame(screenState.value.frameId)
        val cellInfo = ArrayList<CellInfo>()

        for (point in getPoints(position = position)) {
            frame.update(
                point.first,
                point.second,
                color.toArgb()
            )
            if (_screenState.value.matrix[point.first][point.second] != frame.matrix[point.first][point.second]) {
                cellInfo.add(
                    CellInfo(
                        point.first,
                        point.second,
                        _screenState.value.matrix[point.first][point.second]
                    )
                )
            }
            _screenState.value.matrix[point.first][point.second] =
                frame.matrix[point.first][point.second]
        }


        if (cellInfo.isNotEmpty()) {
            val recover = CellRecover(
                info = cellInfo,
                frameId = _screenState.value.frameId,
                onRecover = { info, frameId, action ->
                    onRecoverCells(
                        info = info,
                        frameId = frameId,
                        action = action
                    )
                }
            )
            if (!screenState.value.history.isUndoEmpty) {
                val lastRecover = _screenState.value.history.getUndo()
                if (lastRecover is CellRecover && recover != lastRecover) {
                    _screenState.value.history.addUndo(lastRecover)
                }
            }

            _screenState.value.history.addUndo(recover)
        }
        _screenState.value.history.clearRedo()

        _screenState.value = screenState.value.copy(
            dummyChanges = !screenState.value.dummyChanges
        )
    }

    private fun onPipette(position: Pair<Int, Int>) {
        val frame = _state.value.project.getFrame(screenState.value.frameId)
        val color = Color(frame.matrix[position.first][position.second])
        if (color.alpha != Color.Transparent.alpha) {
            _screenState.value =
                screenState.value.copy(drawColor = color)
        }
    }

    private fun onFiller(position: Pair<Int, Int>) {
        val frame = _state.value.project.getFrame(screenState.value.frameId)
        val cellInfo = ArrayList<CellInfo>()
        for (point in getPoints(position = position)) {
            val set = frame.fillUpdate(
                point.first,
                point.second,
                screenState.value.drawColor.toArgb()
            )

            for (item in set) {
                cellInfo.add(
                    CellInfo(
                        item.first,
                        item.second,
                        _screenState.value.matrix[item.first][item.second]
                    )
                )
                _screenState.value.matrix[item.first][item.second] =
                    frame.matrix[item.first][item.second]
            }
        }

        if (cellInfo.isNotEmpty()) {
            val recover = CellRecover(
                info = cellInfo,
                frameId = _screenState.value.frameId,
                onRecover = { info, frameId, action ->
                    onRecoverCells(
                        info = info,
                        frameId = frameId,
                        action = action
                    )
                }
            )
            if (!screenState.value.history.isUndoEmpty) {
                val lastRecover = _screenState.value.history.getUndo()
                if (lastRecover is CellRecover && recover != lastRecover) {
                    _screenState.value.history.addUndo(
                        lastRecover
                    )
                }
            }

            _screenState.value.history.addUndo(
                recover
            )
        }

        _screenState.value.history.clearRedo()

        _screenState.value = screenState.value.copy(
            dummyChanges = !screenState.value.dummyChanges,
        )
    }

    private fun onRecoverCells(info: List<CellInfo>, frameId: Int, action: Action) {
        val newInfo = ArrayList<CellInfo>()
        val currFrame = _state.value.project.getFrame(frameId)
        for (item in info) {
            newInfo.add(
                CellInfo(
                    item.row,
                    item.column,
                    _screenState.value.matrix[item.row][item.column]
                )
            )
            currFrame.update(
                item.row,
                item.column,
                item.color
            )

            _screenState.value.matrix[item.row][item.column] =
                currFrame.matrix[item.row][item.column]
        }

        _screenState.value = screenState.value.copy(
            dummyChanges = !screenState.value.dummyChanges
        )

        val recover = CellRecover(
            info = newInfo,
            frameId = frameId,
            onRecover = { recoverInfo, recoverFrameId, recoverAction ->
                onRecoverCells(
                    info = recoverInfo,
                    frameId = recoverFrameId,
                    action = recoverAction
                )
            })

        if (action == Action.UNDO) {
            _screenState.value.history.addRedo(recover)
        } else {
            _screenState.value.history.addUndo(recover)
        }
    }

    private fun getPoints(position: Pair<Int, Int>): List<Pair<Int, Int>> {
        return when (screenState.value.mirror) {
            Mirror.OFF -> mutableListOf(position)
            Mirror.X ->
                mutableListOf(
                    position,
                    Pair(position.first, screenState.value.width - 1 - position.second)
                )

            Mirror.Y ->
                mutableListOf(
                    position,
                    Pair(screenState.value.height - position.first - 1, position.second)
                )
            Mirror.FULL -> mutableListOf(
                position,
                Pair(position.first, screenState.value.width - 1 - position.second),
                Pair(screenState.value.height - position.first - 1, position.second),
                Pair(
                    screenState.value.height - position.first - 1,
                    screenState.value.width - 1 - position.second
                )
            )
        }
    }

    //endregion

    //region Project settings
    private fun onUpdateTitle(name: String) {
        viewModelScope.launch {
            try {
                _state.value = state.value.copy(
                    projectEntity = state.value.projectEntity.copy(
                        name = name,
                        lastModified = LocalDateTime.now().toString()
                    )
                )
                useCases.updateProject(state.value.projectEntity)
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

    private fun onSaveProject() {
        viewModelScope.launch {
            if (!screenState.value.algorithmExecuting) {
                _state.value = state.value.copy(
                    projectEntity = state.value.projectEntity.copy(
                        preview = BitmapUtilities.createBitmap(
                            matrix = state.value.project.getFrame(0).matrix,
                            width = state.value.project.width,
                            height = state.value.project.height,
                            scale = 1
                        ).toBase64(),
                        frames = state.value.project.toFrameContainer().toJson(),
                        lastModified = LocalDateTime.now().toString()
                    )
                )
                useCases.updateProject(state.value.projectEntity)
            }
        }
    }

    private fun onChangeZoom(zoom: Int) {
        _screenState.value = screenState.value.copy(
            scaling = min(
                min(25, state.value.projectEntity.width / 4),
                max(1, screenState.value.scaling + zoom)
            )
        )
    }
    //endregion

    //region History
    private fun onUndo() {
        if (!_screenState.value.history.isUndoEmpty) {
            _screenState.value.history.getUndo().recover(Action.UNDO)
            _screenState.value = screenState.value.copy(
                dummyChanges = !screenState.value.dummyChanges,
            )
        }
    }

    private fun onRedo() {
        if (!_screenState.value.history.isRedoEmpty) {
            _screenState.value.history.getRedo().recover(Action.REDO)
            _screenState.value = screenState.value.copy(
                dummyChanges = !screenState.value.dummyChanges,
            )
        }
    }
    //endregion

    sealed class UIEvent {
        data class ShowMessage(val message: String) : UIEvent()
        object UpdateProject : UIEvent()
    }
}