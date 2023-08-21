plugins {
    id("plantscan.android.library")
    id("plantscan.android.hilt")
}

android {
    namespace = "com.github.andiim.plantscan.core.data.test"
}

dependencies {
    api(project(":core:data"))
    implementation(project(":core:testing"))
    implementation(project(":core:common"))
}