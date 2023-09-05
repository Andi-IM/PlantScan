import com.github.andiim.plantscan.app.PsBuildType

plugins {
    id("plantscan.android.application")
    id("plantscan.android.application.firebase")
    id("plantscan.android.application.compose")
    id("plantscan.android.application.flavors")
    id("plantscan.android.hilt")
}

android {
    defaultConfig {
        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME

        testInstrumentationRunner = "com.github.andiim.plantscan.app.PlantScanTestRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = PsBuildType.DEBUG.applicationIdSuffix
        }
        val release by getting {
            isMinifyEnabled = false
            applicationIdSuffix = PsBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        create("benchmark") {
            initWith(release)
            matchingFallbacks.add("release")
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles("benchmark-rules.pro")
            isMinifyEnabled = true
            applicationIdSuffix = PsBuildType.BENCHMARK.applicationIdSuffix
        }
    }

    lint {
        warningsAsErrors = true
        abortOnError = true
        baseline = File("lint-baseline.xml")
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        animationsDisabled = true
    }

    namespace = "com.github.andiim.plantscan.app"
}

dependencies {
    // Logging
    implementation(libs.timber)
    implementation(libs.espresso.idlingResource)

    // GSON
    implementation(libs.gson)

    // Datetime
    implementation(libs.kotlinx.datetime)

    // Retrofit
    implementation(libs.bundles.retrofit)

    // UI
    implementation(libs.coil)
    implementation(libs.material)
    implementation(libs.bundles.paging)

    // Tensorflow
    implementation(libs.bundles.tensorflow)
    // Camera
    implementation(libs.bundles.camera)

    // Hilt
    implementation(libs.dagger.hilt.navigation.compose)
    implementation(libs.constraintlayout)

    // Compose
    // implementation(platform(libs.compose.bom))
    api(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.bundles.compose.debug)

    // Accompanist
    implementation(libs.accompanist.permission)
    implementation(libs.accompanist.webview)

    // Navigation
    implementation(libs.bundles.navigation)

    // Firebase
    // implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.play.services.auth)

    // compat
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.kotlin.coroutines.test.turbine)
    testImplementation(libs.dagger.hilt.testing)
    testImplementation(libs.androidx.mockito)
    testImplementation(libs.androidx.mockito.inline)
    testImplementation(libs.androidx.mockito.android)
    testImplementation(libs.paging.testing)


    // Instrument test
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.ext.junit.ktx)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.intent)
    androidTestImplementation(libs.dagger.hilt.testing)
    androidTestImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.paging.testing)
    androidTestImplementation(libs.navigation.testing)
    androidTestImplementation(libs.androidx.mockito.dexmaker)
}
