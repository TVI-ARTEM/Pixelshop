package ru.pixelshop.database.data.use_case.cases

import kotlinx.coroutines.flow.Flow
import ru.pixelshop.database.data.entities.ProjectEntity
import ru.pixelshop.database.data.repository.ProjectRepository

class GetProjects(private val repo: ProjectRepository) {
    operator fun invoke(): Flow<List<ProjectEntity>> {
        return repo.getProjects()
    }
}