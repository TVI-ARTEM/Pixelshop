package ru.pixelshop.database.data.use_case

import ru.pixelshop.database.data.use_case.cases.*

data class ProjectUseCases(
    val addProject: AddProject,
    val deleteProject: DeleteProject,
    val deleteProjectById: DeleteProjectById,
    val deleteProjects: DeleteProjects,
    val getProjects: GetProjects,
    val getTemplates: GetTemplates,
    val getProject: GetProject,
    val updateProject: UpdateProject,
)
