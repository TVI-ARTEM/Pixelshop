package ru.pixelshop.entities

data class MatrixSize(
    val width: Int,
    val height: Int
) {
    constructor(size:Int) : this(size, size)

    init {
        require(width > 0 && height > 0) {
            "Width and height must be more than 0, was: width = $width, height = $height"
        }
    }

    override fun toString() = "${width}x${height}"
}