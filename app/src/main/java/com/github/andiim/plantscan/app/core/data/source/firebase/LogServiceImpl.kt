package com.github.andiim.plantscan.app.core.data.source.firebase

import com.github.andiim.plantscan.app.core.domain.usecase.firebaseServices.LogService
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class LogServiceImpl @Inject constructor() : LogService {
    override fun logNonFatalCrash(throwable: Throwable) =
        Firebase.crashlytics.recordException(throwable)
}
