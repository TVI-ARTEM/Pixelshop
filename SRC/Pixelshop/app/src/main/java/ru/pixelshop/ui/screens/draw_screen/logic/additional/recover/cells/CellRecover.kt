package ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.cells

import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.Action
import ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.Recoverable

class CellRecover(
    private val info: List<CellInfo>,
    private val frameId: Int, val onRecover: (List<CellInfo>, Int, Action) -> Unit
) :
    Recoverable {
    override fun recover(action: Action) {
        onRecover(info, frameId, action)
    }

    override fun equals(other: Any?): Boolean {
        if (other is CellRecover) {
            return info.size == other.info.size && info.containsAll(other.info) && frameId == other.frameId
        }
        return false
    }
}