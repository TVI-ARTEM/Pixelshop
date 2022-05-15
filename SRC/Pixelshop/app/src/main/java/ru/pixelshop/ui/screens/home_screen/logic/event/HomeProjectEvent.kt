package ru.pixelshop.ui.screens.home_screen.logic.event

sealed class HomeProjectEvent {
    data class ChangedTitle(val value: String) : HomeProjectEvent()
    data class ChangedSize(val value: String) : HomeProjectEvent()
    data class DeleteProject(val value: Long) : HomeProjectEvent()
    data class UpdateTitle(val id: Long, val value: String) : HomeProjectEvent()
    data class CreateProjectTemplate(val value: String) : HomeProjectEvent()
    object CreateProject : HomeProjectEvent()
}
