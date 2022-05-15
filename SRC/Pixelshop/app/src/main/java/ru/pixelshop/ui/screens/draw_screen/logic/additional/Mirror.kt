package ru.pixelshop.ui.screens.draw_screen.logic.additional

enum class Mirror {
    OFF, X, Y, FULL;

    fun getNext(): Mirror {
        return when (this) {
            OFF -> X
            X -> Y
            Y -> FULL
            FULL -> OFF
        }
    }
}