plugins {
    id("plantscan.android.feature")
    id("plantscan.android.library.compose")
    id("plantscan.android.library.jacoco")
}

android {
    namespace = "com.github.andiim.plantscan.feature.category"
}

dependencies {
    implementation(project(":core:crashlytics"))
}