package com.example.playlistmaker.settings.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.playlistmaker.R

@Composable
fun SettingsLine(@StringRes title: Int, onClick: () -> Unit = {}, content: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(true) { onClick() }
            .padding(vertical = dimensionResource(R.dimen.menu_option_padding))
    ) {
        Text(
            text = stringResource(title),
            style = TextStyle(
                fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.font_size_small).toSp() },
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.onSurface,
            ),
            modifier = Modifier.weight(1f)
        )

        content()
    }
}

@Preview
@Composable
private fun SettingsLinePreview() {
    SettingsLine(R.string.settings_theme) {
        Switch(
            checked = true,
            onCheckedChange = {}
        )
    }
}
