plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.jacoco)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.github.andiim.plantscan.core.auth"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.kotlin.coroutines.android)
}
