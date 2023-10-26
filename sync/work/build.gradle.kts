plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.jacoco)
    alias(libs.plugins.android.hilt)
}

android {
    defaultConfig {
        testInstrumentationRunner = "com.github.andiim.plantscan.core.testing.AppTestRunner"
    }
    namespace = "com.github.andiim.plantscan.sync.work"

}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(libs.firebase.storage)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.firebase.cloud.messaging)
    implementation(libs.androidx.hilt.work)
    implementation(libs.kotlin.coroutines.android)
    ksp(libs.hilt.compiler)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
    androidTestImplementation(libs.androidx.work.testing)
}