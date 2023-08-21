plugins {
    id("plantscan.android.library")
    id("plantscan.android.library.compose")
    id("plantscan.android.hilt")
}

android {
    namespace = "com.github.andiim.plantscan.core.crashlytics"
}

dependencies {

    implementation(project(":core:ui"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.coroutines.android)
}