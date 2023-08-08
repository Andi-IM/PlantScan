import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    kotlin("android") apply false
    kotlin("kapt") apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.dagger.hilt.android") apply false
    id("com.google.firebase.crashlytics") apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
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
