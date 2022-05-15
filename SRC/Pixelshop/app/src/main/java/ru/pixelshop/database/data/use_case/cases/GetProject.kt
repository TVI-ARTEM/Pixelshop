package ru.pixelshop.database.data.use_case.cases

import ru.pixelshop.database.data.entities.ProjectEntity
import ru.pixelshop.database.data.repository.ProjectRepository

class GetProject(private val repo: ProjectRepository) {
    suspend operator fun invoke(itemId: Long): ProjectEntity? {
        return repo.getProjectById(itemId)
    }
}