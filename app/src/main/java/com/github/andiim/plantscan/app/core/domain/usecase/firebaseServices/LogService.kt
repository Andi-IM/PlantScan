package com.github.andiim.plantscan.app.core.domain.usecase.firebaseServices

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
