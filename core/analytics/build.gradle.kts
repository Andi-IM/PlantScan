plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.compose)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.github.andiim.plantscan.core.analytics"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.compose.runtime)
    implementation(libs.androidx.core.ktx)
    implementation(libs.firebase.analytics)
    implementation(libs.kotlin.coroutines.android)
}
