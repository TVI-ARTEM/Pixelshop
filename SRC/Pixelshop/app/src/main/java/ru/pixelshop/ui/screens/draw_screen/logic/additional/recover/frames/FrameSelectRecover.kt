package ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.frames

import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.Action
import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.Recoverable

class FrameSelectRecover(private val frameId: Int, val onRecover: (Int, Action) -> Unit) :
    Recoverable {
    override fun recover(action: Action) {
        onRecover(frameId, action)
    }
}