plugins {
    alias(libs.plugins.android.feature)
    alias(libs.plugins.android.library.compose)
    alias(libs.plugins.android.library.jacoco)
}

android {
    namespace = "com.github.andiim.plantscan.feature.history"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.timber)
}
