plugins {
    alias(libs.plugins.android.feature)
    alias(libs.plugins.android.library.compose)
    alias(libs.plugins.android.library.jacoco)
    id("kotlin-parcelize")
}

android {
    namespace = "com.github.andiim.plantscan.feature.detect"
}

dependencies {
    implementation(project(":core:storage-upload"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.timber)
}
