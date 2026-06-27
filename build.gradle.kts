import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt


buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.google.oss.licenses.plugin) {
            exclude(group = "com.google.protobuf")
        }
        classpath(libs.agp)
        classpath(libs.kgp)
        classpath(libs.dhp)
        classpath(libs.ksp.plugin)
        classpath(libs.firebase.crashlytics.plugin)
        classpath(libs.firebase.perf.plugin)
        classpath(libs.firebase.appdistribution.plugin)
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    kotlin("android") apply false
    kotlin("kapt") apply false
    id("com.google.gms.google-services") version "4.5.0" apply false
    id("com.google.dagger.hilt.android") apply false
    id("com.google.firebase.crashlytics") apply false
    id("com.google.firebase.firebase-perf") version "2.0.2" apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.jvm.library) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.modulegraph)
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.versions)
    alias(libs.plugins.com.android.test) apply false
    id("project-report")
    alias(libs.plugins.com.android.application) apply false
    base
}

allprojects {
    group = PUBLISHING_GROUP
}

val detektFormatting: Provider<MinimalExternalModuleDependency> = libs.detekt.formatting
val detektCompose: Provider<MinimalExternalModuleDependency> = libs.detekt.compose

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
        plugin("dev.iurysouza.modulegraph")
    }
    detekt {
        autoCorrect = true
        buildUponDefaultConfig = true
        allRules = false
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    }
    dependencies {
        detektPlugins(detektFormatting)
        detektPlugins(detektCompose)
    }

    moduleGraphConfig {
        readmePath.set("./README.md")
        heading.set("### Dependency Diagram")
        theme.set(dev.iurysouza.modulegraph.Theme.NEUTRAL)
    }
}

tasks {
    withType<DependencyUpdatesTask>().configureEach {
        rejectVersionIf {
            candidate.version.isStableVersion().not()
        }
    }

    withType<Detekt>().configureEach {
        jvmTarget = "1.8"

        reports {
            html.required = true
        }
    }

    withType<Sign>().configureEach{
        notCompatibleWithConfigurationCache("https://github.com/gradle/gradle/issues/13470")
    }

    named("check").configure {
        this.setDependsOn(this.dependsOn.filterNot {
            it is TaskProvider<*> && it.name == "detekt"
        })
    }
}


