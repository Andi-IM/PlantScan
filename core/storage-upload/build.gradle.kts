plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.jacoco)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.secrets)
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.github.andiim.plantscan.core.storageUpload"
}

secrets {
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:notifications"))
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)
    implementation(libs.kotlin.coroutines.android)
}
