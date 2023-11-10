import com.github.andiim.plantscan.app.PsBuildType
import com.google.firebase.appdistribution.gradle.firebaseAppDistribution

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.application.compose)
    alias(libs.plugins.android.application.flavors)
    alias(libs.plugins.android.application.jacoco)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.android.application.firebase)
    alias(libs.plugins.protobuf)
    kotlin("kapt")
    id("jacoco")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    defaultConfig {
        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME

        testInstrumentationRunner = "com.github.andiim.plantscan.core.testing.PsAppTestRunner"
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
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("debug")

            firebaseAppDistribution {
                artifactType = "APK"
            }
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

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        animationsDisabled = true
    }

    lint {
        baseline = file("lint-baseline.xml")
    }

    namespace = "com.github.andiim.plantscan.app"
}

dependencies {
    implementation(project(":feature:findplant"))
    implementation(project(":feature:plant"))
    implementation(project(":feature:plantDetail"))
    implementation(project(":feature:history"))
    implementation(project(":feature:settings"))

    implementation(project(":feature:camera"))
    implementation(project(":feature:detect"))
    implementation(project(":feature:detect-detail"))
    implementation(project(":feature:suggest"))
    implementation(project(":feature:account"))

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))

    implementation(project(":core:auth"))
    implementation(project(":core:analytics"))
    implementation(project(":core:storage-upload"))
    implementation(project(":core:firestore"))

    androidTestImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:datastore-test"))
    androidTestImplementation(project(":core:data-test"))
    androidTestImplementation(project(":core:network"))
    androidTestImplementation(libs.navigation.testing)
    androidTestImplementation(libs.accompanist.testharness)
    androidTestImplementation(kotlin("test"))

    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(project(":ui-test-hilt-manifest"))

    implementation(libs.camera)
    implementation(libs.camera.core)
    implementation(libs.camera.view)

    implementation(libs.compose.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.compose.runtimeTracing)
    implementation(libs.compose.materialWindow)
    implementation(libs.dagger.hilt.navigation.compose)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.window)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.coil)
    implementation(libs.timber)
}
