plugins {
    alias(libs.plugins.android.feature)
    alias(libs.plugins.android.library.compose)
    alias(libs.plugins.android.library.jacoco)
}

android {
    namespace = "com.github.andiim.plantscan.feature.account"
}

dependencies {
    implementation(project(":core:auth"))
}