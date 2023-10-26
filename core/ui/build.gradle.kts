plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.compose)
    alias(libs.plugins.android.library.jacoco)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.github.andiim.plantscan.core.ui"
}

dependencies {
    api(libs.compose.foundation)
    api(libs.compose.foundation.layout)
    api(libs.compose.material)
    api(libs.compose.materialIcons)
    api(libs.compose.runtime)
    api(libs.compose.runtimeLivedata)
    api(libs.compose.ui.tooling.preview)
    api(libs.compose.ui.util)
    api(libs.metrics.performance)
    api(libs.androidx.tracing.ktx)
    api(libs.navigation.ui)

    debugApi(libs.compose.ui.tooling)

    implementation(project(":core:analytics"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(libs.androidx.browser)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.datetime)

    androidTestImplementation(project(":core:testing"))
}