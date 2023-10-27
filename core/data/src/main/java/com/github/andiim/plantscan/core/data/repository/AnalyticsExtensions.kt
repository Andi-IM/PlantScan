package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.core.analytics.AnalyticsEvent
import com.github.andiim.core.analytics.AnalyticsHelper

fun AnalyticsHelper.logOnboardingStateChanged(shouldHideOnboarding: Boolean) {
    val eventType = if (shouldHideOnboarding) "onboarding_complete" else "onboarding_reset"
    logEvent(
        AnalyticsEvent(type = eventType),
    )
}
