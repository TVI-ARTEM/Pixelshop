package ru.pixelshop.ui.screens.draw_screen.logic.state

import android.graphics.drawable.AnimationDrawable
import androidx.compose.ui.graphics.Color
import ru.pixelshop.ui.screens.draw_screen.logic.additional.Mirror
import ru.pixelshop.ui.screens.draw_screen.logic.additional.Tool
import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.History
import ru.pixelshop.utils.ProjectUtilities

data class DrawViewScreenState(
    val frameId: Int = 0,
    val drawColor: Color = Color(0, 0, 0),
    val toolsVisible: Boolean = false,
    val animationVisible: Boolean = false,
    val colorDialogVisible: Boolean = false,
    val algorithmVisible: Boolean = false,
    val scaling: Int = 1,
    val border: Boolean = true,
    val tool: Tool = Tool.PENCIL,
    val mirror: Mirror = Mirror.OFF,
    val matrix: MutableList<MutableList<Int>> = ProjectUtilities.getDummyMatrix(8, 8),
    val dummyChanges: Boolean = false,
    val height: Int = 8,
    val width: Int = 8,
    val history: History = History(1000.toUInt()),
    val settingsOpen: Boolean = false,
    val frameSettingsOpen: Boolean = false,
    val algorithmExecuting: Boolean = false,
    val gifShowing: Boolean = false,
    val gifCreating: Boolean = false,
    val gif: AnimationDrawable = AnimationDrawable(),
)