plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.jacoco)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.github.andiim.plantscan.core.storageUpload"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:notifications"))
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)
    implementation(libs.kotlin.coroutines.android)
}
