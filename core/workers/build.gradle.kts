plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.jacoco)
    alias(libs.plugins.android.hilt)
}

android {
    defaultConfig {
        testInstrumentationRunner = "com.com.github.andiim.plantscan.core.testing.PsAppTestRunner"
    }
    namespace = "com.github.andiim.plantscan.core.workers"
}

dependencies {
    implementation(project(":core:storage-upload"))
    implementation(project(":core:common"))
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    implementation(libs.kotlin.coroutines.android)
    ksp(libs.hilt.compiler)
    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
    androidTestImplementation(libs.androidx.work.testing)
}
