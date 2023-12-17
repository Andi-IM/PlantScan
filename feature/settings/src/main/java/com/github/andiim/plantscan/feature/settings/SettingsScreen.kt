package com.github.andiim.plantscan.feature.settings

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.andiim.plantscan.core.designsystem.component.MinimalDialog
import com.github.andiim.plantscan.core.designsystem.component.PsTextButton
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.designsystem.theme.supportsDynamicTheming
import com.github.andiim.plantscan.core.model.data.DarkThemeConfig
import com.github.andiim.plantscan.feature.settings.R.string
import com.github.andiim.plantscan.feature.settings.components.SettingsButton
import com.github.andiim.plantscan.feature.settings.components.SettingsButtonWithAlert
import com.github.andiim.plantscan.feature.settings.components.SettingsChooser
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@Composable
fun SettingsRoute(
    routeToAuth: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    SettingsScreen(
        uiState = uiState,
        isDialogShow = viewModel.showDialog,
        onAuthAction = routeToAuth,
        onSignOutAction = viewModel::signOut,
        onDeleteAccountAction = viewModel::deleteAccount,
        onChangeDynamicColorPreference = viewModel::updateDynamicColorPreference,
        onChangeDarkThemeConfig = viewModel::updateDarkThemeConfig,
    )
}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    isDialogShow: Boolean,
    onAuthAction: () -> Unit,
    onSignOutAction: () -> Unit,
    onDeleteAccountAction: () -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
) {
    Box {
        LazyColumn(modifier.padding(horizontal = 16.dp)) {
            when (uiState) {
                SettingsUiState.Loading -> {
                    item {
                        Text(
                            stringResource(string.loading),
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    }
                }

                is SettingsUiState.Success -> settingsPanel(
                    settings = uiState.settings,
                    supportDynamicColor = supportDynamicColor,
                    onAuthClick = onAuthAction,
                    onSignOutClick = onSignOutAction,
                    onDeleteAccountClick = onDeleteAccountAction,
                    onChangeDynamicColorPreference = onChangeDynamicColorPreference,
                    onChangeDarkThemeConfig = onChangeDarkThemeConfig,
                )
            }
            item {
                HorizontalDivider(Modifier.padding(top = 8.dp))
            }
            item {
                LinksPanel()
            }
        }
        if (isDialogShow) {
            MinimalDialog(message = "Loading...")
        }
    }
}

fun LazyListScope.settingsPanel(
    settings: UserEditableSettings,
    supportDynamicColor: Boolean,
    onAuthClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
) {
    item {
        AnimatedVisibility(supportDynamicColor) {
            SettingsChooser(
                checked = settings.useDynamicColor,
                title = stringResource(string.dynamic_color_preference),
                onCheckedChange = onChangeDynamicColorPreference,
            )
        }
    }
    darkModeSettings(settings, onChangeDarkThemeConfig)
    accountButtons(settings, onAuthClick, onSignOutClick, onDeleteAccountClick)
}

private fun LazyListScope.darkModeSettings(
    settings: UserEditableSettings,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
) {
    item {
        SettingsDialogSectionTitle(text = stringResource(string.dark_mode_preference))
        Column(Modifier.selectableGroup()) {
            SettingsDialogThemeChooserRow(
                text = stringResource(string.dark_mode_config_system_default),
                selected = settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
                onClick = { onChangeDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM) },
            )
            SettingsDialogThemeChooserRow(
                text = stringResource(string.dark_mode_config_light),
                selected = settings.darkThemeConfig == DarkThemeConfig.LIGHT,
                onClick = { onChangeDarkThemeConfig(DarkThemeConfig.LIGHT) },
            )
            SettingsDialogThemeChooserRow(
                text = stringResource(string.dark_mode_config_dark),
                selected = settings.darkThemeConfig == DarkThemeConfig.DARK,
                onClick = { onChangeDarkThemeConfig(DarkThemeConfig.DARK) },
            )
        }
    }
}

private fun LazyListScope.accountButtons(
    settings: UserEditableSettings,
    onAuthClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
) {
    item {
        SettingsDialogSectionTitle(text = stringResource(string.account))
        if (!settings.isLogin) {
            SettingsButton(
                icon = PsIcons.Account,
                title = stringResource(string.label_sign_in_sign_up),
                onClick = onAuthClick,
            )
        } else {
            SettingsButtonWithAlert(
                icon = PsIcons.Exit,
                title = stringResource(string.sign_out),
                alertTitle = stringResource(string.title_sign_out),
                alertDesc = stringResource(string.description_sign_out),
                onClick = onSignOutClick,
                modifier = Modifier.padding(vertical = 4.dp),
            )
            SettingsButtonWithAlert(
                icon = PsIcons.Delete,
                title = stringResource(string.label_delete_account),
                alertTitle = stringResource(string.title_delete_account),
                alertDesc = stringResource(string.description_delete_account),
                onClick = onDeleteAccountClick,
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LinksPanel() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        val uriHandler = LocalUriHandler.current
        PsTextButton(
            onClick = { uriHandler.openUri(PRIVACY_POLICY_URL) },
        ) {
            Text(text = stringResource(string.privacy_policy))
        }
        val context = LocalContext.current
        PsTextButton(
            onClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
        ) {
            Text(text = stringResource(string.licenses))
        }
        PsTextButton(
            onClick = { uriHandler.openUri(FEEDBACK_URL) },
        ) {
            Text(text = stringResource(string.feedback))
        }
    }
}

@Suppress("detekt:MaxLineLength")
private const val PRIVACY_POLICY_URL = "https://support-orchid.web.app/privacy.html"
private const val FEEDBACK_URL =
    "https://github.com/Andi-IM/PlantScan/issues/new?assignees=&labels=&projects=&template=bug_report.md"
