import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    `kotlin-dsl`
}

group = "com.github.andiim.plantscan.buildsrc"

repositories {
    google()
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.kgp)
    implementation(libs.agp)
    implementation(libs.dhp)
    implementation(libs.firebase.crashlytics.plugin)
    implementation(libs.firebase.perf.plugin)
}

gradlePlugin {
    /** Register convention plugins so they are available in the build scripts of the application */
    plugins {
        register("androidHilt") {
            id = "plantscan.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}
