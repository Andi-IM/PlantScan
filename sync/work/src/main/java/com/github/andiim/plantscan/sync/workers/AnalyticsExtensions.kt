package com.github.andiim.plantscan.sync.workers

import com.github.andiim.core.analytics.AnalyticsEvent
import com.github.andiim.core.analytics.AnalyticsHelper

fun AnalyticsHelper.logSyncStarted() = logEvent(AnalyticsEvent(type = "upload_data_started"))

fun AnalyticsHelper.logSyncFinished(syncedSuccess: Boolean) {
    val eventType = if (syncedSuccess) "upload_data_successful" else "upload_data_failed"
    logEvent(AnalyticsEvent(type = eventType))
}