package com.github.andiim.plantscan.core.testing.util

import com.github.andiim.core.analytics.AnalyticsEvent
import com.github.andiim.core.analytics.AnalyticsHelper

class TestAnalyticsHelper : AnalyticsHelper {
    private val events = mutableListOf<AnalyticsEvent>()
    override fun logEvent(event: AnalyticsEvent) {
        events.add(event)
    }

    fun hasLogged(event: AnalyticsEvent) = events.contains(event)
}