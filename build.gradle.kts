import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.kapt) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.gms) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.firebase.crashlytics) apply false
  alias(libs.plugins.firebase.perf) apply false
  alias(libs.plugins.detekt)
  alias(libs.plugins.versions)
  alias(libs.plugins.secrets) apply false
  base
}

allprojects { group = "com.github.andiim.plantscan" }

val detektFormatting: Provider<MinimalExternalModuleDependency> = libs.detekt.formatting

subprojects {
  apply { plugin("io.gitlab.arturbosch.detekt") }
  detekt { config.setFrom(rootProject.files("config/detekt/detekt.yml")) }
  dependencies { detektPlugins(detektFormatting) }
}

tasks {
  withType<DependencyUpdatesTask>().configureEach {
    rejectVersionIf { candidate.version.isStableVersion().not() }
  }
}

fun String.isStableVersion(): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { uppercase().contains(it) }
  return stableKeyword || Regex("^[0-9,.v-]+(-r)?$").matches(this)
}
