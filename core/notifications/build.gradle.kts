plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.compose)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.github.andiim.core.notifications"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.kotlin.coroutines.android)
    implementation(libs.androidx.browser)
    implementation(libs.compose.runtime)
    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.cloud.messaging)
}