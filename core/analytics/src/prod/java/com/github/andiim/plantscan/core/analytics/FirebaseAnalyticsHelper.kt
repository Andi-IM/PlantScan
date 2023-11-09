
package com.github.andiim.plantscan.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import javax.inject.Inject

/**
 * Implementation of `AnalyticsHelper` which logs events to a Firebase backend.
 */
class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsHelper {

    override fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.type) {
            for (extra in event.extras) {
                // Truncate parameter keys and values according to firebase maximum length values.
                param(
                    key = extra.key.take(40),
                    value = extra.value.take(100),
                )
            }
        }
    }
}