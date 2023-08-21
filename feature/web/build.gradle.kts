plugins {
    id("plantscan.android.feature")
    id("plantscan.android.library.compose")
    id("plantscan.android.library.jacoco")
}

android {
    namespace = "com.github.andiim.plantscan.feature.web"
}

dependencies {
    implementation(project(":core:crashlytics"))
    implementation(libs.accompanist.webview)
}