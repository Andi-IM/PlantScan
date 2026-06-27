plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.jacoco)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.github.andiim.plantscan.core.common"
}

dependencies {
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(project(":core:testing"))
}
