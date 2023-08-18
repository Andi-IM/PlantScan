package com.github.andiim.plantscan.app.core.data.source.firebase.firestore

import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import javax.inject.Inject

class FakeLogServiceImpl @Inject constructor() : LogService {
  override fun logNonFatalCrash(throwable: Throwable) {
    TODO("Not yet implemented")
  }
}
