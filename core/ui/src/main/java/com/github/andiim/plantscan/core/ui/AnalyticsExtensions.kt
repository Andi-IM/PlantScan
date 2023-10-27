package com.github.andiim.plantscan.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.github.andiim.core.analytics.AnalyticsEvent
import com.github.andiim.core.analytics.AnalyticsEvent.Param
import com.github.andiim.core.analytics.AnalyticsEvent.ParamKeys
import com.github.andiim.core.analytics.AnalyticsEvent.Types
import com.github.andiim.core.analytics.AnalyticsHelper
import com.github.andiim.core.analytics.LocalAnalyticsHelper

fun AnalyticsHelper.logScreenView(screenName: String) {
    logEvent(
        AnalyticsEvent(
            type = Types.SCREEN_VIEW,
            extras = listOf(
                Param(ParamKeys.SCREEN_NAME, screenName)
            )
        )
    )
}

// TODO : create function to log item loads

@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
    onDispose { }
}
