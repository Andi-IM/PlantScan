plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.jacoco)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.android.room)
}

android {
    defaultConfig {
        testInstrumentationRunner = "com.github.andiim.plantscan.core.testing.AppTestRunner"
    }
    namespace = "com.github.andiim.plantscan.core.database"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.google.gson)

    androidTestImplementation(project(":core:testing"))
}