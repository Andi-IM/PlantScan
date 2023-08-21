plugins {
    id("plantscan.android.library")
    id("plantscan.android.library.compose")
    id("plantscan.android.library.jacoco")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    lint {
        checkDependencies  = true
    }
    namespace = "com.github.andiim.plantscan.core.designsystem"
}

dependencies {
    api(libs.bundles.compose)
    debugApi(libs.compose.ui.tooling)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)

    androidTestImplementation(project(":core:testing"))
}