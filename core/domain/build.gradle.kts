plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.library.jacoco)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.andiim.plantscan.core.domain"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(libs.hilt.android)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlinx.datetime)

    ksp(libs.hilt.compiler)

    testImplementation(project(":core:testing"))
}