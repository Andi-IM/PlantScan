package com.github.andiim.plantscan.app.core.data.source.firebase

import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService

class FakeLogService : LogService {
  override fun logNonFatalCrash(throwable: Throwable) {
    TODO("Not yet implemented")
  }
}
