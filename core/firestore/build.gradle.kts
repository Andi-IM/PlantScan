plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.jacoco)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.github.andiim.plantscan.core.firestore"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.coil)
    implementation(libs.coil.kt.svg)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(project(":core:testing"))
}
