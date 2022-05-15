package ru.pixelshop.entities

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt


class ColoredMatrix(val width: Int, val height: Int) {
    private val _matrix: MutableList<MutableList<Int>>
    val matrix: List<List<Int>>
        get() {
            return _matrix
        }

    init {
        require(width > 0 && height > 0) {
            "Width and height must be more than 0, was: width = $width, height = $height"
        }

        _matrix = MutableList(height) { MutableList(width) { Color.Transparent.toArgb() } }
    }


    fun update(row: Int, column: Int, colorInt: Int) {
        require(row in 0 until height && column in 0 until width) {
            "0 <= ROW < $height, was: $row; 0 <= COLUMN < $width, was: $row, $column"
        }

        _matrix[row][column] = colorInt
    }

    fun fillUpdate(
        row: Int,
        column: Int,
        colorInt: Int,
    ): Set<Pair<Int, Int>> {
        val set = HashSet<Pair<Int, Int>>()
        fillUpdateInternal(
            row = row,
            column = column,
            prevColorInt = matrix[row][column],
            newColorInt = colorInt,
            set = set
        )

        return set
    }

    private fun fillUpdateInternal(
        row: Int,
        column: Int,
        prevColorInt: Int,
        newColorInt: Int,
        set: HashSet<Pair<Int, Int>>
    ) {
        if (!(row in 0 until height && column in 0 until width)) return

        val currentColor = matrix[row][column]
        val position = Pair(row, column)
        if (set.contains(position) ||
            currentColor != prevColorInt ||
            newColorInt == currentColor
        ) {
            return
        }
        set.add(position)
        _matrix[row][column] = newColorInt
        for (item in listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))) {
            fillUpdateInternal(
                row = row + item.first,
                column = column + item.second,
                prevColorInt = prevColorInt,
                newColorInt = newColorInt,
                set = set
            )
        }
    }


    fun clear(): Set<Pair<Int, Int>> {
        val set = HashSet<Pair<Int, Int>>()
        for (row in _matrix.indices) {
            for (column in _matrix[row].indices) {
                if (Color(_matrix[row][column]).alpha != Color.Transparent.alpha) {
                    _matrix[row][column] = Color.Transparent.toArgb()
                    set.add(Pair(row, column))
                }
            }
        }

        return set
    }
}