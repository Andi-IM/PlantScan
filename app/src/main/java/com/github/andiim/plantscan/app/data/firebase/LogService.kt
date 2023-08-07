package com.github.andiim.plantscan.app.data.firebase

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}