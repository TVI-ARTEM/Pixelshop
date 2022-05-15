package ru.pixelshop.utils

import android.graphics.Bitmap
import android.os.Environment
import com.bilibili.burstlinker.BurstLinker
import java.io.File


class GifUtilities {
    companion object {
        fun saveGif(
            bitmaps: List<Pair<Double, Bitmap>>,
            name: String,
            width: Int,
            height: Int
        ) {
            val burstLinker = BurstLinker()
            try {
                val baseDir: String =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
                val imageFolder = File(baseDir, "Pixelshop")
                imageFolder.mkdir()
                val fileName = "$name.${FileExtension.GIF.extension}"
                val animatedGifFile = File(imageFolder, fileName)
                burstLinker.init(width, height, animatedGifFile.absolutePath)
                for (item in bitmaps) {
                    burstLinker.connect(
                        item.second, BurstLinker.OCTREE_QUANTIZER,
                        BurstLinker.NO_DITHER, 0, 0, (item.first * 1000).toInt()
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                burstLinker.release()
            }
        }
    }
}