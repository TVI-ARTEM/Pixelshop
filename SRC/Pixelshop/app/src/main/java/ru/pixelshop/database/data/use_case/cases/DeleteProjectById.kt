package ru.pixelshop.database.data.use_case.cases

import ru.pixelshop.database.data.repository.ProjectRepository

class DeleteProjectById (private val repository: ProjectRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteProject(id)
    }
}