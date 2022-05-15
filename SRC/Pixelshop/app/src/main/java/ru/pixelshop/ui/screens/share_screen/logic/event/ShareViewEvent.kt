package ru.pixelshop.ui.screens.share_screen.logic.event

sealed class ShareViewEvent {
    data class ChangeScale(val value: Int) : ShareViewEvent()
    data class ChangeFrame(val value: Int) : ShareViewEvent()
    object ExportPNG : ShareViewEvent()
    object ExportGIF : ShareViewEvent()
}
