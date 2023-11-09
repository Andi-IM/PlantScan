plugins {
    alias(libs.plugins.android.feature)
    alias(libs.plugins.android.library.compose)
    alias(libs.plugins.android.library.jacoco)
}

android {
    namespace = "com.github.andiim.plantscan.feature.settings"
}

dependencies {
    implementation(project(":core:auth"))
    implementation(libs.androidx.appcompat)
    implementation(libs.google.oss.licenses){
        exclude(group = "androidx.appcompat")
    }
}