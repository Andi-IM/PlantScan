package com.github.andiim.plantscan.app.core.domain.usecase.firebase_services

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}