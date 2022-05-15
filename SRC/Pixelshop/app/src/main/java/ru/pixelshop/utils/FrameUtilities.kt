package ru.pixelshop.utils

import ru.pixelshop.entities.Frame
import ru.pixelshop.utils.BitmapUtilities.Companion.toBitmap
import ru.pixelshop.utils.BitmapUtilities.Companion.toMatrix

class FrameUtilities {
    companion object {
        fun String.toFrame(speed: Double, width: Int, height: Int) : Frame {
            val bitmapMatrix = this.toBitmap().toMatrix()
            val frame = Frame(width = width, height = height, _speed = speed)
            for (row in 0 until height) {
                for (column in 0 until width) {
                    frame.update(row = row, column = column, colorInt = bitmapMatrix[row][column])
                }
            }

            return frame
        }
    }
}