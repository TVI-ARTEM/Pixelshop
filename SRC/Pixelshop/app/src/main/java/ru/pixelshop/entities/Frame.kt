package ru.pixelshop.entities


class Frame(val width: Int, val height: Int, _speed: Double) {
    private val _matrix: ColoredMatrix = ColoredMatrix(width, height)
    val matrix: List<List<Int>>
        get() = _matrix.matrix

    var speed = _speed
        set(value) {
            require(value >= 0.0) {
                "Sped must be non negative, was: $value"
            }
            field = value
        }


    fun update(row: Int, column: Int, colorInt: Int) {
        _matrix.update(row, column, colorInt)
    }

    fun fillUpdate(row: Int, column: Int, colorInt: Int): Set<Pair<Int, Int>> {
        return _matrix.fillUpdate(row, column, colorInt)
    }
}