package ru.pixelshop.ui.screens.draw_screen.ui.panels

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.pixelshop.R
import ru.pixelshop.ui.customs.IconWithShadow
import ru.pixelshop.ui.customs.TextWithShadow
import ru.pixelshop.ui.screens.draw_screen.logic.state.DrawViewScreenState

@Composable
fun BottomAlgorithmPanel(
    screenState: DrawViewScreenState,
    onPlay: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(100.dp).copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize
        ),
        color = MaterialTheme.colors.secondaryVariant
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 50.dp)

        ) {
            TextWithShadow(
                text = stringResource(R.string.algorithm_text),
                style = MaterialTheme.typography.body1.copy(fontSize = 20.sp),
                letterSpacing = 5.sp,
                offsetX = 2.dp,
                offsetY = 2.dp,
                alpha = 0.1f,
                color = MaterialTheme.colors.primary,
            )
            Spacer(modifier = Modifier.width(20.dp))
            IconButton(onClick = onPlay, modifier = Modifier.size(42.dp)) {
                IconWithShadow(
                    painter = painterResource(
                        id = if (screenState.algorithmExecuting)
                            R.drawable.ic_pause
                        else
                            R.drawable.ic_anim_play_btn
                    ),
                    description = "play animation",
                    mainTint = MaterialTheme.colors.primary,
                    offsetX = 1.dp,
                    offsetY = 1.dp,
                    alpha = 0.1f
                )
            }
        }
    }
}
