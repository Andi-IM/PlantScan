package com.github.andiim.plantscan.app

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class PsBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
    BENCHMARK(".benchmark")
}
