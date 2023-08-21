plugins {
  id("plantscan.android.library")
  id("plantscan.android.library.jacoco")
  id("plantscan.android.hilt")
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
  defaultConfig {
    testInstrumentationRunner = "com.github.andiim.plantscan.core.testing.PlantScanTestRunner"
  }
  buildFeatures { buildConfig = true }
  namespace = "com.github.andiim.plantscan.core.firestore"
}

secrets {
  defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:model"))

  api(libs.tensorflow.support)
  implementation(libs.timber)

  kapt(libs.hilt.compiler)

  implementation(platform(libs.firebase.bom))
  implementation(libs.bundles.firebase)
  implementation(libs.tensorflow.downloader)
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

  testImplementation(project(":core:testing"))
  androidTestImplementation(project(":core:testing"))
}
