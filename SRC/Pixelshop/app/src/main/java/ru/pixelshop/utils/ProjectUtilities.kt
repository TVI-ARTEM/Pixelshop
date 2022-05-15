package ru.pixelshop.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.gson.Gson
import ru.pixelshop.database.data.entities.FrameContainer
import ru.pixelshop.entities.Project
import ru.pixelshop.database.data.entities.ProjectEntity
import ru.pixelshop.utils.BitmapUtilities.Companion.toBase64
import ru.pixelshop.utils.FrameUtilities.Companion.toFrame
import java.time.LocalDateTime
import kotlin.math.max
import kotlin.math.min

class ProjectUtilities {
    companion object {
        fun getDummyProjectEntity(): ProjectEntity {
            val gson = Gson()
            val dummyProject = getDummyProject()
            return ProjectEntity(
                id = -1L,
                name = "dummy",
                height = 8,
                width = 8,
                preview = gson.toJson(dummyProject.getFrame(0).matrix),
                frames = gson.toJson(dummyProject),
                lastModified = LocalDateTime.now().toString()
            )
        }

        fun getDummyProject(): Project {
            return Project(8, 8)
        }

        fun getDummyMatrix(rows: Int, columns: Int): MutableList<MutableList<Int>> {
            return MutableList(rows) { MutableList(columns) { Color.Black.toArgb() } }
        }

        fun getPosition(rows: Int, columns: Int, tapOffset: Offset, width: Int): Pair<Int, Int> {
            val cellSize = width * 1.0 / columns
            val rowIndex = max(0, min(rows - 1, (tapOffset.y / cellSize).toInt()))
            val columnIndex = max(0, min(columns - 1, (tapOffset.x / cellSize).toInt()))
            return Pair(rowIndex, columnIndex)
        }

        fun FrameContainer.toProject(width: Int, height: Int): Project {
            val frames =
                list.map { it.second.toFrame(speed = it.first, width = width, height = height) }
            val initializeFrame = frames[0]
            val project = Project(width = width, height = height, frame = initializeFrame)
            for (frame in frames.drop(1)) {
                project.addFrame(frame = frame)
            }
            return project
        }

        fun Project.toFrameContainer(): FrameContainer {
            val list: MutableList<Pair<Double, String>> = mutableListOf()
            for (frame in frames) {
                list.add(
                    Pair(
                        frame.speed,
                        BitmapUtilities.createBitmap(
                            matrix = frame.matrix,
                            width = width,
                            height = height,
                            scale = 1
                        ).toBase64()
                    )
                )
            }

            return FrameContainer(list = list)
        }

        fun FrameContainer.toJson(): String {
            return Gson().toJson(this)
        }

        fun String.toFrameContainer(): FrameContainer {
            return Gson().fromJson(this, FrameContainer::class.java)
        }
    }
}