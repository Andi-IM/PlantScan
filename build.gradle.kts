import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import dev.iurysouza.modulegraph.ModuleGraphExtension
import dev.iurysouza.modulegraph.ModuleGraphPlugin
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
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.application.compose) apply false
    alias(libs.plugins.android.application.jacoco) apply false
    alias(libs.plugins.android.application.firebase) apply false
    alias(libs.plugins.android.application.flavors) apply false
    alias(libs.plugins.android.feature) apply false
    alias(libs.plugins.android.hilt) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.library.compose) apply false
    alias(libs.plugins.android.library.jacoco) apply false
    alias(libs.plugins.android.room) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.jvm.library) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.modulegraph)
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.versions)
    base
}

allprojects {
    group = PUBLISHING_GROUP
}

val detektFormatting: Provider<MinimalExternalModuleDependency> = libs.detekt.formatting

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
    dependencies { detektPlugins(detektFormatting) }

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
}


