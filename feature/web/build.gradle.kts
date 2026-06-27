plugins {
    alias(libs.plugins.android.feature)
    alias(libs.plugins.android.library.compose)
    alias(libs.plugins.android.library.jacoco)
}

android {
    namespace = "com.github.andiim.plantscan.feature.web"
}

dependencies {
    implementation(libs.accompanist.webview)
}