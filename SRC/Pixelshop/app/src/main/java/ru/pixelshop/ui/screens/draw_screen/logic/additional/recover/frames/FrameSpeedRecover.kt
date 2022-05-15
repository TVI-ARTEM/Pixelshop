package ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.frames

import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.Action
import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.Recoverable

class FrameSpeedRecover(
    private val frameId: Int,
    private val speed: Double,
    val onRecover: (Int, Double, Action) -> Unit
) :
    Recoverable {
    override fun recover(action: Action) {
        onRecover(frameId, speed, action)
    }

}