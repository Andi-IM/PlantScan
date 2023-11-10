plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.compose)
    alias(libs.plugins.android.library.jacoco)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    lint {
        checkDependencies = true
    }
    namespace = "com.github.andiim.plantscan.core.designsystem"
}

dependencies {
    lintPublish(project(":lint"))

    api(libs.compose.foundation)
    api(libs.compose.foundation.layout)
    api(libs.compose.materialIcons)
    api(libs.compose.material)
    api(libs.compose.runtime)
    api(libs.compose.ui.tooling.preview)
    api(libs.compose.ui.util)

    debugApi(libs.compose.ui.tooling)

    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)

    androidTestImplementation(project(":core:testing"))
}