plugins {
    id("plantscan.android.library")
    id("plantscan.android.library.jacoco")
    id("plantscan.android.hilt")
}

android {
    namespace = "com.github.andiim.plantscan.core.common"
}

dependencies {
    testImplementation(project(":core:testing"))
}