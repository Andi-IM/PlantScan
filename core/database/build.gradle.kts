plugins {
    id("plantscan.android.library")
    id("plantscan.android.library.jacoco")
    id("plantscan.android.hilt")
    id("plantscan.android.room")
}

android {
    defaultConfig {
        testInstrumentationRunner = "com.github.andiim.plantscan.core.testing.PlantScanTestRunner"
    }
    namespace = "com.github.andiim.plantscant.core.database"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.datetime)

    androidTestImplementation(project(":core:testing"))
}