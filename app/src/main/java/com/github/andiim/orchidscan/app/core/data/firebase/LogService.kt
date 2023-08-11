package com.github.andiim.orchidscan.app.data.firebase

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}