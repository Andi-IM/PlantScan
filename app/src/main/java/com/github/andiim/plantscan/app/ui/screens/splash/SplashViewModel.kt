package com.github.andiim.plantscan.app.ui.screens.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.github.andiim.plantscan.app.core.auth.AccountService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.ConfigurationService
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    private val logService: LogService,
) : ViewModel() {

    val showError = mutableStateOf(false)

    init {
        launchCatching(logService) { configurationService.fetchConfiguration() }
    }

    fun onAppStart(openScreen: () -> Unit) {
        showError.value = false
        if (accountService.hasUser) openScreen()
        else {
            launchCatching(logService = logService, snackbar = true) {
                try {
                    accountService.createAnonymousAccount()
                } catch (ex: Exception) {
                    showError.value = true
                    throw ex
                }
                openScreen()
            }
        }
    }
}

