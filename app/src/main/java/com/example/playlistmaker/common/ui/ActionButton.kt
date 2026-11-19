package com.example.playlistmaker.common.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionButton(@StringRes textRes: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(6.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .wrapContentSize()
            .clip(
                RoundedCornerShape(54.dp)
            )
    ) {
        Text(
            text = stringResource(textRes), style = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.surface,
                fontWeight = FontWeight(500)
            )
        )
    }
}
