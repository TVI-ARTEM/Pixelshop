package ru.pixelshop.ui.screens.draw_screen.logic.event

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import ru.pixelshop.ui.screens.draw_screen.logic.additional.Tool

sealed class DrawViewEvent {
    data class UpdateTitle(val value: String) : DrawViewEvent()
    data class SelectFrame(val value: Int) : DrawViewEvent()
    data class OnCells(val offset: Offset, val width: Int) : DrawViewEvent()
    data class ChangeZoom(val value: Int) : DrawViewEvent()
    data class ChangeTool(val value: Tool) : DrawViewEvent()
    data class ChangeColor(val value: Color) : DrawViewEvent()
    data class ChangeSpeed(val value: Double) : DrawViewEvent()
    data class ChangePosition(val value: Int) : DrawViewEvent()
    object RemoveFrame : DrawViewEvent()
    object AddFrame : DrawViewEvent()
    object ChangeGridVisible : DrawViewEvent()
    object ChangeToolVisible : DrawViewEvent()
    object ChangeAnimationVisible : DrawViewEvent()
    object ChangeAlgorithmVisible : DrawViewEvent()
    object ChangeColorDialogVisible : DrawViewEvent()
    object ChangeSettingsVisible : DrawViewEvent()
    object ChangeFrameSettingsVisible : DrawViewEvent()
    object ChangeMirror : DrawViewEvent()
    object Undo : DrawViewEvent()
    object Redo : DrawViewEvent()
    object SaveProject : DrawViewEvent()
    object AlgorithmExecute : DrawViewEvent()
    object AlgorithmStop : DrawViewEvent()
    object AlgorithmStart : DrawViewEvent()
    object ShowGif : DrawViewEvent()
    object StopPlayingGif : DrawViewEvent()
}