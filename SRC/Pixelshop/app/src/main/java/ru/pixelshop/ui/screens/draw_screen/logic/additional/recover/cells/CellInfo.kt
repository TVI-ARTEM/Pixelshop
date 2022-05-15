package ru.pixelshop.ui.screens.draw_screen.logic.additional.recover.cells

data class CellInfo(
    val row: Int, val column: Int,
    val color: Int
) {
    override fun equals(other: Any?): Boolean {
        if (other is CellInfo) {
            return row == other.row && column == other.column && color == other.column
        }
        return false
    }
}
