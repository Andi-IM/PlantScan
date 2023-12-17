plugins {
    alias(libs.plugins.android.feature)
    alias(libs.plugins.android.library.compose)
    alias(libs.plugins.android.library.jacoco)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.github.andiim.plantscan.feature.findplant"
}

dependencies {
    implementation("com.algolia:instantsearch-compose:3.3.0")
    implementation("com.algolia:instantsearch-android-paging3:3.3.0")
}
