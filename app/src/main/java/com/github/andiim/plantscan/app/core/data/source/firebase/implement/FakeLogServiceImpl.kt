package com.github.andiim.plantscan.app.core.data.source.firebase.implement

import com.github.andiim.plantscan.app.core.data.source.firebase.LogService
import javax.inject.Inject

class FakeLogServiceImpl @Inject constructor(): LogService {
    override fun logNonFatalCrash(throwable: Throwable) {
        TODO("Not yet implemented")
    }
}