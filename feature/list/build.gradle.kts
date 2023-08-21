plugins {
  id("plantscan.android.feature")
  id("plantscan.android.library.jacoco")
  id("plantscan.android.library.compose")
}

android { namespace = "com.github.andiim.plantscan.feature.list" }

dependencies { implementation(project(":core:crashlytics")) }
