package com.example.playlistmaker.common.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.ui.theme.Theme

@Composable
fun ScreenLayout(@StringRes title: Int, showBackButton: Boolean, content: @Composable () -> Unit) {
    val navController = rememberNavController()

    Theme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.padding_default))
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = dimensionResource(R.dimen.title_height))
                    .clickable(enabled = showBackButton) {
                        navController.navigateUp()
                    }) {

                if (showBackButton) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(48.dp, 48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back_48),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_small)))
                }

                Text(
                    text = stringResource(title),
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyle(
                        fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.font_size).toSp() },
                        fontWeight = FontWeight(500),
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                )
            }

            content()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ScreenLayoutPreview() {
    ScreenLayout(title = R.string.new_playlist, showBackButton = true) {
        Text("Aboba")
    }
}
