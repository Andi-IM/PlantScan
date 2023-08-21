plugins {
  id("plantscan.android.feature")
  id("plantscan.android.library.compose")
  id("plantscan.android.library.jacoco")
}

android { namespace = "com.github.andiim.plantscan.feature.home" }

dependencies {
  implementation(project(":core:crashlytics"))
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
}
