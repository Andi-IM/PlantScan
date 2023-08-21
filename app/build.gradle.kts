import com.github.andiim.plantscan.app.PlantScanBuildType

plugins {
  id("plantscan.android.application")
  id("plantscan.android.application.compose")
  id("plantscan.android.application.flavors")
  id("plantscan.android.application.jacoco")
  id("plantscan.android.hilt")
  id("jacoco")
  id("plantscan.android.application.firebase")
}

android {
  compileSdk = libs.versions.compile.sdk.version.get().toInt()
  defaultConfig {
    minSdk = libs.versions.min.sdk.version.get().toInt()
    targetSdk = libs.versions.target.sdk.version.get().toInt()

    applicationId = AppCoordinates.APP_ID
    versionCode = AppCoordinates.APP_VERSION_CODE
    versionName = AppCoordinates.APP_VERSION_NAME
    testInstrumentationRunner = "com.github.andiim.plantscan.core.testing.PlantScanTestRunner"
    vectorDrawables { useSupportLibrary = true }
  }
  buildFeatures {
    viewBinding = true
    compose = true
    buildConfig = true
  }
  composeOptions { kotlinCompilerExtensionVersion = libs.versions.compose.compiler.extension.get() }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions { jvmTarget = JavaVersion.VERSION_17.toString() }

  buildTypes {
    debug { applicationIdSuffix = PlantScanBuildType.DEBUG.applicationIdSuffix }
    val release by getting {
      isMinifyEnabled = false
      applicationIdSuffix = PlantScanBuildType.RELEASE.applicationIdSuffix
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      // To publish on the Play store a private signing key is required, but to allow anyone
      // who clones the code to sign and run the release variant, use the debug signing key.
      // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
      signingConfig = signingConfigs.getByName("debug")
    }
    create("benchmark") {
      // Enable all the optimizations from release build through initWith(release).
      initWith(release)
      matchingFallbacks.add("release")
      // Debug key signing is available to everyone.
      signingConfig = signingConfigs.getByName("debug")
      // Only use benchmark proguard rules
      proguardFiles("benchmark-rules.pro")
      isMinifyEnabled = true
      applicationIdSuffix = PlantScanBuildType.BENCHMARK.applicationIdSuffix
    }
  }
  lint {
    warningsAsErrors = true
    abortOnError = true
    baseline = File("lint-baseline.xml")
  }
  testOptions {
    unitTests { isIncludeAndroidResources = true }
    animationsDisabled = true
  }
  namespace = "com.github.andiim.plantscan.app"
}

dependencies {
  implementation(project(":feature:splash"))
  implementation(project(":feature:home"))
  implementation(project(":feature:web"))
  implementation(project(":feature:camera"))

  implementation(project(":core:common"))
  implementation(project(":core:ui"))
  implementation(project(":core:designsystem"))
  implementation(project(":core:data"))
  implementation(project(":core:model"))

  androidTestImplementation(project(":core:testing"))
  androidTestImplementation(project(":core:data-test"))
  androidTestImplementation(project(":core:firestore"))
  androidTestImplementation(libs.navigation.testing)
  androidTestImplementation(kotlin("test"))
  debugImplementation(libs.compose.ui.test.manifest)
  debugImplementation(project(":ui-test-hilt-manifest"))

  implementation(libs.androidx.profileinstaller)

  // Logging
  implementation(libs.timber)

  // UI
  implementation(libs.coil)
  implementation(libs.material)
  implementation(libs.bundles.paging)

  // Hilt
  implementation(libs.hilt.android)
  implementation(libs.hilt.navigation.compose)
  kapt(libs.hilt.compiler)

  // Compose
  implementation(platform(libs.compose.bom))
  api(libs.bundles.compose)
  implementation(libs.bundles.lifecycle)
  androidTestImplementation(libs.compose.ui.test)
  debugImplementation(libs.bundles.compose.debug)
  implementation(libs.lifecycle.runtime.compose)

  // Accompanist
  implementation(libs.accompanist.permission)

  // Navigation
  implementation(libs.bundles.navigation)

  // compat
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core.ktx)
}
