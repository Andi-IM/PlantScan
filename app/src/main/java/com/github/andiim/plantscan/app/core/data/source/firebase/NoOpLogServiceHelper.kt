package com.github.andiim.plantscan.app.core.data.source.firebase

import com.github.andiim.plantscan.app.core.domain.usecase.firebaseServices.LogService

class NoOpLogServiceHelper : LogService {
    override fun logNonFatalCrash(throwable: Throwable) = Unit
}