package ru.pixelshop.utils

import ru.pixelshop.entities.MatrixSize
import java.time.LocalDateTime
import java.util.*

class Utilities {
    companion object {
        fun Double.format(digits: Int) = "%.${digits}f".format(this)

        fun Int.pow(x: Int): Int = (2..x).fold(this) { r, _ -> r * this }

        fun String.parseDate(): String {
            val params = this.lowercase(Locale.getDefault()).split("t")
            require(params.size == 2) {
                "Incorrect amount of parameters, must have 2, was: ${params.size}"
            }

            if (params[0] == LocalDateTime.now().toString().lowercase(Locale.getDefault())
                    .split("t")[0]
            ) {
                return params[1].split(".")[0]
            }

            return params[0].replace("-", "/")
        }

        fun String.toMatrixSize(): MatrixSize {
            val params = this.split("x")
            require(params.size == 2) {
                "Incorrect amount of parameters, must have 2, was: ${params.size}"
            }
            return MatrixSize(params[0].toInt(), params[1].toInt())
        }
  }
}