package com.github.andiim.plantscan.app.ui.screens.home.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.ui.common.composables.BasicToolbar
import com.github.andiim.plantscan.app.ui.common.composables.DangerousCardEditor
import com.github.andiim.plantscan.app.ui.common.composables.DialogCancelButton
import com.github.andiim.plantscan.app.ui.common.composables.DialogConfirmButton
import com.github.andiim.plantscan.app.ui.common.composables.RegularCardEditor
import com.github.andiim.plantscan.app.ui.common.extensions.card
import com.github.andiim.plantscan.app.ui.common.extensions.spacer
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme

@Composable
fun SettingsElement(
    restartApp: (String) -> Unit,
    routeToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isAnonymity = viewModel.uiState.collectAsState(SettingsUiState(false))
    SettingsContent(
        modifier = modifier,
        restartApp = restartApp,
        isAnonymity = isAnonymity.value.isAnonymousAccount,
        onLoginClick = routeToLogin,
        onSignOutClick = viewModel::onSignOutClick,
        onDeleteMyAccountClick = viewModel::onDeleteMyAccountClick
    )
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    isAnonymity: Boolean = false,
    restartApp: (String) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onSignOutClick: ((String) -> Unit) -> Unit = {},
    onDeleteMyAccountClick: ((String) -> Unit) -> Unit = {}
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .testTag("Settings Content")
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicToolbar(title = R.string.label_settings)
        Spacer(modifier = Modifier.spacer())
        if (isAnonymity) {
            RegularCardEditor(
                R.string.label_sign_in_sign_up,
                R.drawable.ic_sign_in,
                "",
                Modifier
                    .card()
                    .testTag("To Sign In Button"),
                onClick = onLoginClick
            )
        } else {
            SignOutCard { onSignOutClick(restartApp) }
            DeleteMyAccountCard { onDeleteMyAccountClick(restartApp) }
        }
    }
}

@Composable
private fun SignOutCard(signOut: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    RegularCardEditor(
        title = R.string.label_sign_out,
        icon = R.drawable.ic_exit,
        content = "",
        Modifier
            .card()
            .testTag("Sign Out Card")
    ) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            modifier = Modifier.testTag("Sign Out Warning Dialog"),
            onDismissRequest = { showWarningDialog = false },
            title = { Text(stringResource(R.string.title_sign_out)) },
            text = { Text(stringResource(R.string.description_sign_out)) },
            dismissButton = { DialogCancelButton(R.string.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(R.string.label_sign_out) {
                    signOut()
                    showWarningDialog = false
                }
            })
    }
}

@Composable
private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    DangerousCardEditor(
        title = R.string.label_delete_account,
        icon = R.drawable.ic_delete_my_account,
        content = "",
        modifier = Modifier
            .card()
            .testTag("Delete Account Card")
    ) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            modifier = Modifier.testTag("Delete Account Warning Dialog"),
            onDismissRequest = { showWarningDialog = false },
            title = { Text(stringResource(R.string.title_delete_account)) },
            text = { Text(stringResource(R.string.description_delete_account)) },
            dismissButton = { DialogCancelButton(R.string.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(R.string.label_delete_account) {
                    deleteMyAccount()
                    showWarningDialog = false
                }
            })
    }
}

@Preview
@Composable
private fun Preview_SettingsContent() {
    PlantScanTheme { Surface { SettingsContent() } }
}
