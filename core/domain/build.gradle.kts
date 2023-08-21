plugins {
    id("plantscan.android.library")
    id("plantscan.android.library.jacoco")
    kotlin("kapt")
}

android {
    namespace = "com.github.andiim.plantscan.core.domain"
}

dependencies {
    implementation(project(":core:common")) // gaboleh common
    implementation(project(":core:data"))
    implementation(project(":core:model"))

    api(libs.paging.common)
    implementation(libs.hilt.android)
    implementation(libs.kotlin.coroutines.android)

    kapt(libs.hilt.compiler)

    testImplementation(project(":core:testing"))
}