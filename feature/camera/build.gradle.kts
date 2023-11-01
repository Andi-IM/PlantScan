plugins {
    alias(libs.plugins.android.feature)
    alias(libs.plugins.android.library.compose)
    alias(libs.plugins.android.library.jacoco)
}

android {
    buildFeatures {
        viewBinding = true
    }
    namespace = "com.github.andiim.plantscan.feature.camera"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.bundles.camera)
    implementation(libs.accompanist.permission)
}
