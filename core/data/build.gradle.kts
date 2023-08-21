plugins {
    id("plantscan.android.library")
    id("plantscan.android.library.jacoco")
    id("plantscan.android.hilt")
}

android {
    namespace = "com.github.andiim.plantscan.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:firestore"))
    implementation(project(":core:model"))
    implementation(libs.paging.common)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.datetime)

    testImplementation(project(":core:testing"))
}
