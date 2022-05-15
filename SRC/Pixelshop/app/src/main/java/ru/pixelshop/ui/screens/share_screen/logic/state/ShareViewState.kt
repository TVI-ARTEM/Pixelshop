package ru.pixelshop.ui.screens.share_screen.logic.state

import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.res.ResourcesCompat
import ru.pixelshop.PixelshopApp
import ru.pixelshop.R

data class ShareViewState(
    val frames: List<Pair<Double, Bitmap>> = listOf(
        Pair(
            1.0, (ResourcesCompat.getDrawable(
                PixelshopApp.context.resources,
                R.drawable.mario_templates,
                null
            ) as BitmapDrawable).bitmap
        )
    ),
    val framesScaled: List<Pair<Double, Bitmap>> = listOf(
        Pair(
            1.0, (ResourcesCompat.getDrawable(
                PixelshopApp.context.resources,
                R.drawable.mario_templates,
                null
            ) as BitmapDrawable).bitmap
        )
    ),
    val gif: AnimationDrawable = AnimationDrawable(),
    val frameId: Int = 0,
    val scale: Int = 1,
    val width: Int = 0,
    val height: Int = 0,
    val name: String = "PROJECT",
    val gifCreating: Boolean = false
)