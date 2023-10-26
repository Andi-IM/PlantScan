import com.github.andiim.plantscan.app.PsBuildType
import com.google.firebase.appdistribution.gradle.firebaseAppDistribution

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.application.firebase)
    alias(libs.plugins.android.application.compose)
    id("plantscan.android.application.flavors")
    id("plantscan.android.application.jacoco")
    alias(libs.plugins.protobuf)
    id("plantscan.android.hilt")
    id("org.jetbrains.kotlin.kapt")
    id("jacoco")
    id("kotlinx-serialization")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    defaultConfig {
        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME

        targetSdk = libs.versions.target.sdk.version.get().toInt()

        testInstrumentationRunner = "com.github.andiim.plantscan.app.PlantScanTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = PsBuildType.DEBUG.applicationIdSuffix
        }
        val release by getting {
            isMinifyEnabled = true
            applicationIdSuffix = PsBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")

            firebaseAppDistribution {
                artifactType = "APK"
            }
        }
        /*create("benchmark") {
            initWith(release)
            matchingFallbacks.add("release")
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles("benchmark-rules.pro")
            isMinifyEnabled = true
            applicationIdSuffix = PsBuildType.BENCHMARK.applicationIdSuffix
        }*/
    }

    lint {
        // warningsAsErrors = true
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

secrets {
    defaultPropertiesFileName = "secrets.defaults.properties"
}

// Setup protobuf configuration, generating lite Java and Kotlin classes
protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach {task ->
            task.builtins{
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

androidComponents.beforeVariants {
    android.sourceSets.register(it.name) {
        java.srcDir(buildDir.resolve("generated/source/proto/${it.name}/java"))
        kotlin.srcDir(buildDir.resolve("generated/source/proto/${it.name}/kotlin"))
    }
}

dependencies {
    // Logging
    implementation(libs.timber)
    implementation(libs.espresso.idlingResource)

    // Profile Installer
    implementation(libs.androidx.profileinstaller)
    // Guava
    implementation(libs.kotlinx.coroutines.guava)

    // Datetime
    implementation(libs.kotlinx.datetime)

    // Retrofit
    implementation(libs.bundles.retrofit)

    // UI
    implementation(libs.coil)
    implementation(libs.coil.kt.svg)
    implementation(libs.material)
    implementation(libs.material.window)
    implementation(libs.bundles.paging)
    implementation(libs.androidx.core.splashscreen)

    // Camera
    implementation(libs.bundles.camera)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)

    // Hilt
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    implementation(libs.dagger.hilt.navigation.compose)
    implementation(libs.constraintlayout)

    // Compose
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.metrics.performance)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.bundles.compose.debug)

    // Accompanist
    implementation(libs.accompanist.permission)
    implementation(libs.accompanist.webview)

    // Navigation
    implementation(libs.bundles.navigation)

    // Firebase
    implementation(libs.bundles.firebase)
    implementation(libs.play.services.auth)

    // datastore
    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    // compat
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.truth)
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
