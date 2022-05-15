package ru.pixelshop.ui.screens.home_screen.logic.state

import ru.pixelshop.entities.MatrixSize

data class HomeViewProjectState(
    val size: MatrixSize = MatrixSize(1, 1),
    val title: String = "",
)
