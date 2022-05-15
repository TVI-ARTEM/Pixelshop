package ru.pixelshop.database.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.pixelshop.database.data.database.ProjectDatabase
import ru.pixelshop.database.data.repository.ProjectRepository
import ru.pixelshop.database.data.repository.ProjectRepositoryImpl
import ru.pixelshop.database.data.use_case.*
import ru.pixelshop.database.data.use_case.cases.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): ProjectDatabase {
        return ProjectDatabase.getInstance(application = application)
    }

    @Provides
    @Singleton
    fun provideRepository(database: ProjectDatabase): ProjectRepository {
        return ProjectRepositoryImpl(database.projectDao())
    }

    @Provides
    @Singleton
    fun provideProjectUseCases(repo: ProjectRepository): ProjectUseCases {
        return ProjectUseCases(
            addProject = AddProject(repo),
            deleteProject = DeleteProject(repo),
            deleteProjectById = DeleteProjectById(repo),
            deleteProjects = DeleteProjects(repo),
            getProject = GetProject(repo),
            getProjects = GetProjects(repo),
            getTemplates = GetTemplates(repo),
            updateProject = UpdateProject(repo),
        )
    }
}