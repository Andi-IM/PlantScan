package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.analytics.AnalyticsEvent
import com.github.andiim.plantscan.core.analytics.AnalyticsEvent.Param
import com.github.andiim.plantscan.core.analytics.AnalyticsHelper

fun AnalyticsHelper.logOnboardingStateChanged(shouldHideOnboarding: Boolean) {
    val eventType = if (shouldHideOnboarding) "onboarding_complete" else "onboarding_reset"
    logEvent(
        AnalyticsEvent(type = eventType),
    )
}

fun AnalyticsHelper.logDarkThemeConfigChanged(darkThemeConfigName: String) =
    logEvent(
        AnalyticsEvent(
            type = "dark_theme_config_changed",
            extras = listOf(
                Param(key = "dark_theme_config", value = darkThemeConfigName),
            ),
        ),
    )

fun AnalyticsHelper.logDynamicColorPreferenceChanged(useDynamicColor: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "dynamic_color_preference_changed",
            extras = listOf(
                Param(key = "dynamic_color_preference", value = useDynamicColor.toString()),
            ),
        ),
    )

fun AnalyticsHelper.logLoginInfo(userId: String, isAnonymous: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "login_info_preference_changed",
            extras = listOf(
                Param(key = "userId", value = userId),
                Param(key = "asAnonymous", value = isAnonymous.toString())
            )
        )
    )
