plugins {
    id("plantscan.android.library")
    id("plantscan.android.library.compose")
    id("plantscan.android.hilt")
}

android {
    namespace = "com.github.andiim.plantscan.core.testing"
}

dependencies {

    api(libs.compose.ui.test)
    api(libs.androidx.test.core)
    api(libs.espresso.core)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)
    api(libs.hilt.android.testing)
    api(libs.junit)
    api(libs.truth)
    api(libs.kotlin.coroutines.test)
    api(libs.kotlin.coroutines.test.turbine)

    debugApi(libs.compose.ui.test.manifest)

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(libs.kotlin.datetime)
}