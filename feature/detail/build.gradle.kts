plugins {
    id("plantscan.android.feature")
    id("plantscan.android.library.jacoco")
    id("plantscan.android.library.compose")
}

android {
    namespace = "com.github.andiim.plantscan.feature.detail"
}

dependencies {
    implementation(project(":core:crashlytics"))
}