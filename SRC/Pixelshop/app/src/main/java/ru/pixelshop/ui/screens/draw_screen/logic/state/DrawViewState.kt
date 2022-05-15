package ru.pixelshop.ui.screens.draw_screen.logic.state

import ru.pixelshop.entities.Project
import ru.pixelshop.database.data.entities.ProjectEntity
import ru.pixelshop.utils.ProjectUtilities

data class DrawViewState(
    val title: String = "",
    val projectEntity: ProjectEntity = ProjectUtilities.getDummyProjectEntity(),
    val project: Project = ProjectUtilities.getDummyProject()
)