package com.github.andiim.plantscan.app.data.firebase.implement

import com.github.andiim.plantscan.app.data.firebase.LogService
import javax.inject.Inject

class LogServiceImpl @Inject constructor(): LogService {
    override fun logNonFatalCrash(throwable: Throwable) {
        TODO("Not yet implemented")
    }
}