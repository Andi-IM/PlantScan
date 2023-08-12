package com.github.andiim.plantscan.app.core.data.source.firebase

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}