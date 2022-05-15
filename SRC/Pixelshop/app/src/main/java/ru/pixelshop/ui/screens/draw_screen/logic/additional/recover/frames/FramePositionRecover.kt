package ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.frames

import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.Action
import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.Recoverable

class FramePositionRecover(
    private val frameId: Int,
    private val prevFrameId: Int,
    val onRecover: (Int, Int, Action) -> Unit
) :
    Recoverable {
    override fun recover(action: Action) {
        onRecover(frameId, prevFrameId, action)
    }
}