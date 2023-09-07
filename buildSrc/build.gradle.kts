import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    `kotlin-dsl`
}

group = "com.github.andiim.plantscan.buildsrc"

repositories {
    google()
    mavenCentral()
}

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
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
    implementation(libs.ksp.plugin)
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
        register("androidApplication"){
            id = "plantscan.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidFirebase"){
            id = "plantscan.android.application.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidApplicationJacoco") {
            id = "plantscan.android.application.jacoco"
            implementationClass = "AndroidApplicationJacocoConventionPlugin"
        }
        register("androidCompose"){
            id = "plantscan.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidFlavors") {
            id = "plantscan.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
    }
}
