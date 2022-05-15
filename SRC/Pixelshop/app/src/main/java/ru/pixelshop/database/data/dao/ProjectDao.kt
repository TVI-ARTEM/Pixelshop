package ru.pixelshop.database.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.pixelshop.database.data.entities.ProjectEntity
import ru.pixelshop.database.data.entities.TemplateEntity

@Dao
interface ProjectDao {
    @Query("SELECT * FROM ProjectData")
    fun getProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM TemplateData")
    fun getTemplates(): Flow<List<TemplateEntity>>

    @Query("SELECT * FROM ProjectData WHERE id = :id")
    suspend fun getProjectById(id: Long): ProjectEntity?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(project: ProjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: TemplateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<ProjectEntity>)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("DELETE FROM ProjectData WHERE id = :id")
    suspend fun deleteProject(id: Long)

    @Query("DELETE FROM ProjectData")
    suspend fun deleteProjects()
}