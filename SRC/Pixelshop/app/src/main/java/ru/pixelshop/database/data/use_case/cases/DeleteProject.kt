package ru.pixelshop.database.data.use_case.cases

import ru.pixelshop.database.data.entities.ProjectEntity
import ru.pixelshop.database.data.repository.ProjectRepository

class DeleteProject(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(item: ProjectEntity) {
        repository.deleteProject(item)
    }
}