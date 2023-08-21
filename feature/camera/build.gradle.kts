plugins {
  id("plantscan.android.feature")
  id("plantscan.android.library.compose")
  id("plantscan.android.hilt")
}

android {
  namespace = "com.github.andiim.plantscan.feature.camera"
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.fragment)

  implementation(libs.bundles.paging)
  implementation(libs.constraintlayout)

  implementation(platform(libs.compose.bom))
  api(libs.bundles.compose)
  implementation(libs.bundles.camera)
  implementation(libs.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.recyclerview)
}
