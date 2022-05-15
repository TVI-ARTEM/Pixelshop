package ru.pixelshop.ui.screens.share_screen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import ru.pixelshop.ui.screens.share_screen.logic.state.ShareViewState

@Composable
fun ShowDrawable(
    gifExport: MutableState<Boolean>,
    state: ShareViewState
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .aspectRatio(1f)
            .border(
                width = 5.dp,
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
    ) {
        if (!gifExport.value) {
            Image(
                bitmap = state.framesScaled[state.frameId].second.asImageBitmap(),
                contentDescription = "pic"
            )
        } else {
            Image(
                painter = rememberDrawablePainter(state.gif),
                contentDescription = "gif",
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit
            )
            state.gif.start()
        }
    }
}