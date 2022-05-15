package ru.pixelshop.database.data.repository

import kotlinx.coroutines.flow.Flow
import ru.pixelshop.database.data.dao.ProjectDao
import ru.pixelshop.database.data.entities.ProjectEntity
import ru.pixelshop.database.data.entities.TemplateEntity

class ProjectRepositoryImpl(private val projectDao: ProjectDao) : ProjectRepository {
    override fun getProjects(): Flow<List<ProjectEntity>> {
        return projectDao.getProjects()
    }

    override fun getTemplates(): Flow<List<TemplateEntity>> {
        return projectDao.getTemplates()
    }

    override suspend fun getProjectById(itemId: Long): ProjectEntity? {
        return projectDao.getProjectById(itemId)
    }

    override suspend fun updateProject(project: ProjectEntity) {
        projectDao.update(project)
    }

    override suspend fun insertProject(project: ProjectEntity) {
        projectDao.insertProject(project)
    }

    override suspend fun insertTemplate(template: TemplateEntity) {
        projectDao.insertTemplate(template)
    }

    override suspend fun insertProjects(projects: List<ProjectEntity>) {
        projectDao.insertProjects(projects)
    }

    override suspend fun deleteProject(project: ProjectEntity) {
        projectDao.deleteProject(project)
    }

    override suspend fun deleteProject(id: Long) {
        projectDao.deleteProject(id)
    }

    override suspend fun deleteProjects() {
        projectDao.deleteProjects()
    }
}