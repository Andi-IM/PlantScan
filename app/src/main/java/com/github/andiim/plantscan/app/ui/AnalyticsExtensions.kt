package com.github.andiim.plantscan.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.github.andiim.plantscan.app.core.analytics.AnalyticsEvent
import com.github.andiim.plantscan.app.core.analytics.AnalyticsEvent.Param
import com.github.andiim.plantscan.app.core.analytics.AnalyticsEvent.ParamKeys
import com.github.andiim.plantscan.app.core.analytics.AnalyticsEvent.Types
import com.github.andiim.plantscan.app.core.analytics.AnalyticsHelper
import com.github.andiim.plantscan.app.core.analytics.LocalAnalyticsHelper

/**
 * Classes and functions associated with analytics events for the UI.
 */
fun AnalyticsHelper.logScreenView(screenName: String) {
    logEvent(
        AnalyticsEvent(
            type = Types.SCREEN_VIEW,
            extras = listOf(
                Param(ParamKeys.SCREEN_NAME, screenName),
            ),
        ),
    )
}

fun AnalyticsHelper.logPlantResourceOpened(plantResourceId: String) {
    logEvent(
        event = AnalyticsEvent(
            type = "plant_resource_opened",
            extras = listOf(
                Param("plant_news_resource", plantResourceId),
            ),
        ),
    )
}

/**
 * A side-effect which records a screen view event.
 */
@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
    onDispose {}
}
