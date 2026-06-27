import com.github.andiim.plantscan.app.PsBuildType
import com.github.andiim.plantscan.app.configureFlavors

plugins {
    alias(libs.plugins.android.test)
}

android {
    namespace = "com.github.andiim.plantscan.benchmarks"

    defaultConfig {
        minSdk = 27
        targetSdk = libs.versions.target.sdk.version.get().toInt()
        compileSdk = libs.versions.compile.sdk.version.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "APP_BUILD_TYPE_SUFFIX", "\"\"")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        // Up to Java 11 APIs are available through desugaring
        // https://developer.android.com/studio/write/java11-minimal-support-table
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It's signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            // Keep the build type debuggable so we can attach a debugger if needed
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
            buildConfigField(
                "String",
                "APP_BUILD_TYPE_SUFFIX",
                "\"${PsBuildType.BENCHMARK.applicationIdSuffix ?: ""}\""
            )
        }
    }

    configureFlavors(this) { flavor ->
        buildConfigField(
            "String",
            "APP_FLAVOR_SUFFIX",
            "\"${flavor.applicationIdSuffix ?: ""}\""
        )
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(libs.benchmark.macro.junit4)
    implementation(libs.androidx.test.core)
    implementation(libs.espresso.core)
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.test.runner)
    implementation(libs.uiautomator)
}

androidComponents {
    beforeVariants {
        it.enable = it.buildType == "benchmark"
    }
}