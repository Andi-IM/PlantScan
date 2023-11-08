plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.github.andiim.plantscan.datastore.test"
}

dependencies {
    api(project(":core:datastore"))
    api(libs.androidx.dataStore.core)

    implementation(libs.protobuf.kotlin.lite)
    implementation(project(":core:common"))
    implementation(project(":core:testing"))
}