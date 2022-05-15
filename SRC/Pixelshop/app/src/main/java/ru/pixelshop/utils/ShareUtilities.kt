package ru.pixelshop.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

class ShareUtilities {
    companion object {
        @SuppressLint("QueryPermissionsNeeded")
        fun exportImage(name: String, extension: FileExtension, context: Context) {
            val baseDir: String =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
            val imageFolder = File(baseDir, "Pixelshop")
            imageFolder.mkdir()
            val fileName = "$name.${extension.extension}"
            val imageFile = File(imageFolder, fileName)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "img/*"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hello, I'm using Pixelshop\nI want to share with my project: \"$name\""
                )
                val fileURI = FileProvider.getUriForFile(
                    context, context.packageName + ".provider",
                    imageFile
                )
                putExtra(Intent.EXTRA_STREAM, fileURI)
            }
            context.startActivity(shareIntent)
        }
    }
}