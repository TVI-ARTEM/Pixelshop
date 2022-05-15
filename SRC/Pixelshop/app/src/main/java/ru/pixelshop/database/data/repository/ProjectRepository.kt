package ru.pixelshop.database.data.repository

import kotlinx.coroutines.flow.Flow
import ru.pixelshop.database.data.entities.ProjectEntity
import ru.pixelshop.database.data.entities.TemplateEntity

interface ProjectRepository {
    fun getProjects(): Flow<List<ProjectEntity>>

    fun getTemplates(): Flow<List<TemplateEntity>>

    suspend fun getProjectById(itemId: Long): ProjectEntity?

    suspend fun updateProject(project: ProjectEntity)

    suspend fun insertProject(project: ProjectEntity)

    suspend fun insertTemplate(template: TemplateEntity)

    suspend fun insertProjects(projects: List<ProjectEntity>)

    suspend fun deleteProject(project: ProjectEntity)

    suspend fun deleteProject(id: Long)

    suspend fun deleteProjects()
}