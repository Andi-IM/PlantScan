package com.github.andiim.plantscan.core.notifications

import javax.inject.Inject

class NoOpNotifier @Inject constructor() : Notifier {
    override fun postNotification() = Unit
}
