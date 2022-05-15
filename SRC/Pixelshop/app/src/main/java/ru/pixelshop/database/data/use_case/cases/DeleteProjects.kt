package ru.pixelshop.database.data.use_case.cases

import ru.pixelshop.database.data.repository.ProjectRepository

class DeleteProjects(private val repo: ProjectRepository) {
    suspend operator fun invoke() {
        repo.deleteProjects()
    }
}