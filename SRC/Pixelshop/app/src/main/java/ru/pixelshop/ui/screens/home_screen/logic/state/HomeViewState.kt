package ru.pixelshop.ui.screens.home_screen.logic.state

import ru.pixelshop.database.data.entities.ProjectEntity

data class HomeViewState(
    val projects: Map<Long, ProjectEntity> = emptyMap(),
    val matrix: Map<Long, List<List<Int>>> = emptyMap(),
    val templatesMatrix: Map<String, List<List<Int>>> = emptyMap()
)
