package com.github.andiim.plantscan.app.ui.common.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.app.ui.common.extensions.alertDialog
import com.github.andiim.plantscan.app.ui.common.extensions.textButton
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import com.github.andiim.plantscan.app.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(onRequestPermission: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            modifier = Modifier.alertDialog(),
            onDismissRequest = { showWarningDialog = false }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        stringResource(AppText.notification_permission_title),
                        color = (MaterialTheme.colorScheme).onSurface,
                        style = (MaterialTheme.typography).headlineSmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        stringResource(AppText.notification_permission_description),
                        style = (MaterialTheme.typography).bodyMedium,
                        color = (MaterialTheme.colorScheme).onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = {
                            onRequestPermission()
                            showWarningDialog = false
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            stringResource(AppText.request_notification_permission),
                            style = (MaterialTheme.typography).labelLarge
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RationaleDialog() {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            modifier = Modifier.alertDialog(),
            onDismissRequest = { showWarningDialog = false }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        stringResource(AppText.notification_permission_title),
                        color = (MaterialTheme.colorScheme).onSurface,
                        style = (MaterialTheme.typography).headlineSmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        stringResource(AppText.notification_permission_description),
                        color = (MaterialTheme.colorScheme).onSurfaceVariant,
                        style = (MaterialTheme.typography).bodyMedium
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = { showWarningDialog = false },
                        modifier = Modifier.textButton()
                    ) {
                        Text(stringResource(AppText.ok))
                    }
                }
            }
        }
    }
}

@Preview(name = "Night Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Day Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun RationalDialogPreview() {
    PlantScanTheme { Surface { RationaleDialog() } }
}

@Preview(name = "Night Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Day Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PermissionDialogPreview() {
    PlantScanTheme { Surface { PermissionDialog {} } }
}
