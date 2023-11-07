package com.github.andiim.plantscan.feature.camera.noPermission

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.designsystem.theme.PsTheme
import com.github.andiim.plantscan.core.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.feature.camera.R

@Composable
fun NoPermissionScreen(
    onBackClick: () -> Unit,
    onRequestPermission: () -> Unit,
) {
    NoPermissionContent(
        onBackClick = onBackClick,
        onRequestPermission = onRequestPermission,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoPermissionContent(
    onBackClick: () -> Unit,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TrackScreenViewEvent(screenName = "Blocked Camera")
    Box(
        modifier = modifier
            .fillMaxSize()
            .animateContentSize(),
    ) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(
                    onClick = onBackClick,
                ) {
                    Icon(
                        PsIcons.Back,
                        contentDescription = stringResource(R.string.back_from_camera),
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            ),
            modifier = Modifier.align(Alignment.TopStart),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center),
        ) {
            Text(text = stringResource(R.string.camera_permission_error_text))
            Button(onClick = onRequestPermission) {
                Icon(imageVector = Icons.Default.Camera, contentDescription = null)
                Text(text = stringResource(R.string.request_permission_text))
            }
        }
    }
}

@Preview
@Composable
fun Preview_NoPermissionContent() {
    PsTheme {
        Surface {
            NoPermissionContent(
                onBackClick = {},
                onRequestPermission = {},
            )
        }
    }
}
