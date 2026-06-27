package com.github.andiim.plantscan.core.testing.util

import com.github.andiim.plantscan.core.data.util.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// NETWORK MONITOR
class TestNetworkMonitor : NetworkMonitor {
    private val connectivityFlow = MutableStateFlow(true)
    override val isOnline: Flow<Boolean> = connectivityFlow

    /**
     * A test-only API to set the connectivity state from tests.
     */
    fun setConnected(isConnected: Boolean) {
        connectivityFlow.value = isConnected
    }
}
