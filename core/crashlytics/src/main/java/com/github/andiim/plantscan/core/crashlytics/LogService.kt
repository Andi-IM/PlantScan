package com.github.andiim.plantscan.core.crashlytics

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}