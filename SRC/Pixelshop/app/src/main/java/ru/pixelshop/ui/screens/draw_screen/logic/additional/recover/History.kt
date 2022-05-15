package ru.pixelshop.ui.screens.draw_screen.logic.additional.recover

import java.util.*

class History(private val limit: UInt) {
    private val undoHistory: Deque<Recoverable> = ArrayDeque()
    private val redoHistory: Deque<Recoverable> = ArrayDeque()
    val isUndoEmpty: Boolean
        get() = undoHistory.isEmpty()
    val isRedoEmpty: Boolean
        get() = redoHistory.isEmpty()

    fun addUndo(history: Recoverable) {
        undoHistory.addFirst(history)
        if (undoHistory.size > limit.toInt() && limit.toInt() != 0) {
            undoHistory.removeLast()
        }
    }

    fun addRedo(history: Recoverable) {
        redoHistory.addFirst(history)
        if (redoHistory.size > limit.toInt() && limit.toInt() != 0) {
            redoHistory.removeLast()
        }
    }

    fun getUndo(): Recoverable {
        return undoHistory.removeFirst()
    }

    fun getRedo(): Recoverable {
        return redoHistory.removeFirst()
    }

    fun clearUndo() {
        undoHistory.clear()
    }

    fun clearRedo() {
        redoHistory.clear()
    }
}