plugins {
  id("plantscan.android.library")
  id("plantscan.android.library.compose")
  id("plantscan.android.library.jacoco")
}

android {
  defaultConfig {
    testInstrumentationRunner = "com.github.andiim.plantscan.core.testing.PlantScanTestRunner"
  }
  namespace = "com.github.andiim.plantscan.core.ui"
}

dependencies {
  api(libs.compose.foundation)
  api(libs.compose.foundation.layout)
  api(libs.bundles.compose)
  api(libs.tracing.ktx)
  api(libs.androidx.metrics)
  api(libs.bundles.paging)

  debugApi(libs.bundles.compose.debug)

  implementation(project(":core:designsystem"))
  implementation(project(":core:domain"))
  implementation(project(":core:model"))
  implementation(libs.androidx.core.ktx)
  implementation(libs.coil)
  implementation(libs.coil.compose)

  androidTestImplementation(project(":core:testing"))
}
