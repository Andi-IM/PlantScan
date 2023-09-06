import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    kotlin("android") apply false
    kotlin("kapt") apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.dagger.hilt.android") apply false
    id("com.google.firebase.crashlytics") apply false
    id("com.google.firebase.firebase-perf") apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.versions)
    base
}

allprojects {
    group = PUBLISHING_GROUP
}

val detektFormatting: Provider<MinimalExternalModuleDependency> = libs.detekt.formatting

subprojects {
    apply { plugin("io.gitlab.arturbosch.detekt") }
    detekt { config.setFrom(rootProject.files("config/detekt/detekt.yml")) }
    dependencies { detektPlugins(detektFormatting) }
}

tasks {
    withType<DependencyUpdatesTask>().configureEach {
        rejectVersionIf {
            candidate.version.isStableVersion().not()
        }
    }
}
