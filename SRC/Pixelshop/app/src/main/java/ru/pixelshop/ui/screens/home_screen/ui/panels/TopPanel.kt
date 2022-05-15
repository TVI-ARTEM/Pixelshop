package ru.pixelshop.ui.screens.home_screen.ui.panels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.pixelshop.R
import ru.pixelshop.ui.customs.TextWithShadow

@Preview
@Composable
fun TopPanel() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(75.dp).copy(
            topStart = ZeroCornerSize,
            topEnd = ZeroCornerSize
        ),
        color = MaterialTheme.colors.secondary
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextWithShadow(
                text = stringResource(id = R.string.app_name_panel),
                style = MaterialTheme.typography.h1,
                letterSpacing = 10.sp,
                offsetX = 4.dp,
                offsetY = 4.dp,
                alpha = 0.25f,
                color = MaterialTheme.colors.primary
            )
        }
    }
}