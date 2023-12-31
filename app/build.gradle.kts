plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    compileSdk = libs.versions.compile.sdk.version.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.version.get().toInt()
        targetSdk = libs.versions.target.sdk.version.get().toInt()
        namespace = "com.github.andiim.plantscan.app"

        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.compose.compilerextension.get() }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = JavaVersion.VERSION_17.toString() }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lint {
        warningsAsErrors = true
        abortOnError = true
        baseline = File("lint-baseline.xml")
    }

    // Use this block to configure different flavors
//    flavorDimensions("version")
//    productFlavors {
//        create("full") {
//            dimension = "version"
//            applicationIdSuffix = ".full"
//        }
//        create("demo") {
//            dimension = "version"
//            applicationIdSuffix = ".demo"
//        }
//    }
}

dependencies {
    // Logging
    implementation(libs.timber)

    // UI
    implementation(libs.material)

    // Hilt
    implementation(libs.dagger.hilt)
    implementation(libs.dagger.hilt.navigation.compose)
    kapt(libs.dagger.hilt.compiler)

    // Ext. Module
    implementation(projects.libraryAndroid)
    implementation(projects.libraryKotlin)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)
    implementation(libs.bundles.lifecycle)

    // Accompanist
    implementation(libs.accompanist.permission)
    implementation(libs.accompanist.webview)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.play.services.auth)

    // compat
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.dagger.hilt.testing)
    kaptTest(libs.dagger.hilt.compiler)

    // Instrument test
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.ext.junit.ktx)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.dagger.hilt.testing)
    androidTestImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.truth)
    kaptAndroidTest(libs.dagger.hilt.compiler)
}

kapt { correctErrorTypes = true }
hilt { enableAggregatingTask = true }