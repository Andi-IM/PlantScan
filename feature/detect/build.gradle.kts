plugins {
  id("plantscan.android.feature")
  id("plantscan.android.library.compose")
  id("plantscan.android.library.jacoco")
}

android { namespace = "com.github.andiim.plantscan.feature.detect" }

dependencies {
  implementation(project(":core:crashlytics"))
  implementation(libs.bundles.tensorflow)
}
