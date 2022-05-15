package ru.pixelshop.database.data.use_case.cases

import ru.pixelshop.PixelshopApp
import ru.pixelshop.R
import ru.pixelshop.database.data.entities.ProjectEntity
import ru.pixelshop.database.data.exceptions.InvalidProjectException
import ru.pixelshop.database.data.repository.ProjectRepository

class UpdateProject(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(item: ProjectEntity) {
        if (item.name.isBlank()) {
            throw InvalidProjectException(PixelshopApp.context.getString(R.string.title_exception))
        }
        repository.updateProject(item)
    }
}