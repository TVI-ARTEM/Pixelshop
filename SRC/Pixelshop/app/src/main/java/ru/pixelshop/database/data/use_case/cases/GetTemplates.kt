package ru.pixelshop.database.data.use_case.cases

import kotlinx.coroutines.flow.Flow
import ru.pixelshop.database.data.entities.TemplateEntity
import ru.pixelshop.database.data.repository.ProjectRepository

class GetTemplates (private val repo: ProjectRepository) {
    operator fun invoke(): Flow<List<TemplateEntity>> {
        return repo.getTemplates()
    }
}