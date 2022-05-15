package ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.frames

import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.Action
import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.Recoverable


class FrameRecover(
    private val frameId: Int,
    private val matrix: List<List<Int>>,
    val onRecover: (Int, List<List<Int>>, Action) -> Unit
) :
    Recoverable {
    override fun recover(action: Action) {
        onRecover(frameId, matrix, action)
    }
}