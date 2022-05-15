package ru.pixelshop.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Base64.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.set
import ru.pixelshop.entities.Project
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min


class BitmapUtilities {
    companion object {
        fun createBitmap(
            matrix: List<List<Int>>,
            width: Int,
            height: Int,
            scale: Int,
            fillTransparent: Boolean = false
        ): Bitmap {
            val bitmap = Bitmap.createBitmap(
                width * scale,
                height * scale,
                Bitmap.Config.ARGB_8888
            )

            for (rowIndex in 0 until height) {
                for (columnIndex in 0 until width) {
                    for (scaleRowIndex in 0 until scale) {
                        for (scaleColumnIndex in 0 until scale) {
                            bitmap[columnIndex * scale + scaleColumnIndex, rowIndex * scale + scaleRowIndex] =
                                if (fillTransparent && Color(matrix[rowIndex][columnIndex]).alpha != 1f)
                                    Color.White.toArgb()
                                else
                                    matrix[rowIndex][columnIndex]
                        }
                    }
                }
            }

            return bitmap
        }

        fun Bitmap.toMatrix(): List<List<Int>> {
            return MutableList(height) { row ->
                MutableList(width) { column ->
                    this.getPixel(
                        column,
                        row
                    )
                }
            }
        }

        fun generateBitmaps(project: Project, scale: Int): List<Pair<Double, Bitmap>> {
            return List(project.framesCount) { index ->
                project.getFrame(index = index).let {
                    Pair(
                        it.speed,
                        createBitmap(
                            matrix = it.matrix,
                            width = project.width,
                            height = project.height,
                            scale = max(1, min(20, scale))
                        )
                    )
                }
            }
        }

        fun saveBitmap(bitmap: Bitmap, name: String) {
            try {
                val baseDir: String =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
                val imageFolder = File(baseDir, "Pixelshop")
                imageFolder.mkdir()
                val fileName = "$name.${FileExtension.PNG.extension}"
                val imageFile = File(imageFolder, fileName)
                val stream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
                stream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun Bitmap.toBase64(): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            return encodeToString(byteArray, DEFAULT)
        }

        fun String.toBitmap(): Bitmap {
            val imageBytes = decode(this, 0)
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }
    }
}