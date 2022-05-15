package ru.pixelshop.ui.screens.share_screen.logic.model

import android.graphics.drawable.AnimationDrawable
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.pixelshop.PixelshopApp
import ru.pixelshop.database.data.use_case.ProjectUseCases
import ru.pixelshop.ui.screens.share_screen.logic.event.ShareViewEvent
import ru.pixelshop.ui.screens.share_screen.logic.state.ShareViewState
import ru.pixelshop.utils.BitmapUtilities
import ru.pixelshop.utils.BitmapUtilities.Companion.toBitmap
import ru.pixelshop.utils.BitmapUtilities.Companion.toMatrix
import ru.pixelshop.utils.FileExtension
import ru.pixelshop.utils.GifUtilities
import ru.pixelshop.utils.ProjectUtilities.Companion.toFrameContainer
import ru.pixelshop.utils.ShareUtilities
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.max

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val useCases: ProjectUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(ShareViewState())
    val state: State<ShareViewState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentProjectId: Long? = null

    init {
        savedStateHandle.get<Long>("projectId")?.let { projectId ->
            if (projectId != -1L) {
                viewModelScope.launch {
                    useCases.getProject(projectId)?.also { projectEntity ->
                        currentProjectId = projectEntity.id

                        val bitmaps = projectEntity.frames.toFrameContainer().list.map {
                            Pair(
                                it.first,
                                it.second.toBitmap()
                            )
                        }
                        val bitmapsScaled = bitmaps.map {
                            Pair(
                                it.first,
                                BitmapUtilities.createBitmap(
                                    matrix = it.second.toMatrix(),
                                    width = projectEntity.width,
                                    height = projectEntity.height,
                                    scale = abs(100 - projectEntity.width) / 2
                                )
                            )
                        }

                        val animation = AnimationDrawable()
                        for (item in bitmapsScaled) {
                            animation.addFrame(
                                item.second.toDrawable(PixelshopApp.context.resources),
                                (item.first * 1000).toInt()
                            )
                        }

                        _state.value = state.value.copy(
                            width = projectEntity.width,
                            height = projectEntity.height,
                            frames = bitmaps,
                            framesScaled = bitmapsScaled,
                            gif = animation,
                            name = projectEntity.name
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: ShareViewEvent) {
        try {
            when (event) {
                is ShareViewEvent.ExportGIF -> onExportGIF()
                is ShareViewEvent.ExportPNG -> onExportPNG()
                is ShareViewEvent.ChangeFrame -> onChangeFrame(event.value)
                is ShareViewEvent.ChangeScale -> onChangeScale(event.value)
            }
        } catch (exception: Exception) {
            Log.i("Exception", exception.message ?: "Something went wrong")
        }
    }

    private fun onExportGIF() {
        CoroutineScope(IO).launch {
            try {
                _state.value = state.value.copy(
                    gifCreating = true
                )
                val scale = if (state.value.width < 32)
                    max(
                        32 / state.value.width,
                        state.value.scale
                    )
                else
                    state.value.scale
                val bitmaps = state.value.frames.map {
                    Pair(
                        it.first,
                        BitmapUtilities.createBitmap(
                            matrix = it.second.toMatrix(),
                            width = state.value.width,
                            height = state.value.height,
                            scale = scale,
                            fillTransparent = true
                        )
                    )
                }

                GifUtilities.saveGif(
                    bitmaps,
                    state.value.name,
                    state.value.width * scale,
                    state.value.height * scale
                )
                _state.value = state.value.copy(
                    gifCreating = false
                )
                ShareUtilities.exportImage(
                    state.value.name,
                    FileExtension.GIF,
                    PixelshopApp.context
                )
            } catch (exception: Exception) {
                _eventFlow.emit(
                    UIEvent.ShowMessage(
                        message = exception.message ?: "Couldn't save project"
                    )
                )
            }

            _state.value = state.value.copy(
                gifCreating = false
            )
        }
    }

    private fun onExportPNG() {
        CoroutineScope(IO).launch {
            try {
                _state.value = state.value.copy(
                    gifCreating = true
                )
                val bitmap = BitmapUtilities.createBitmap(
                    matrix = state.value.frames[state.value.frameId].second.toMatrix(),
                    width = state.value.width,
                    height = state.value.height,
                    scale = state.value.scale
                )
                BitmapUtilities.saveBitmap(
                    bitmap,
                    state.value.name
                )
                _state.value = state.value.copy(
                    gifCreating = false
                )
                ShareUtilities.exportImage(
                    state.value.name,
                    FileExtension.PNG,
                    PixelshopApp.context
                )
            } catch (exception: Exception) {
                _eventFlow.emit(
                    UIEvent.ShowMessage(
                        message = exception.message ?: "Couldn't save project"
                    )
                )
            }

            _state.value = state.value.copy(
                gifCreating = false
            )
        }
    }

    private fun onChangeFrame(id: Int) {
        _state.value = state.value.copy(
            frameId = id
        )
    }

    private fun onChangeScale(value: Int) {
        _state.value = state.value.copy(
            scale = value
        )
    }

    sealed class UIEvent {
        data class ShowMessage(val message: String) : UIEvent()
    }
}