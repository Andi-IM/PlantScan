import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins { `kotlin-dsl` }

group = "com.github.andiim.plantscan.buildlogic"

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
  compileOnly(libs.agp)
  compileOnly(libs.kgp)
  compileOnly(libs.dhp)
  compileOnly(libs.ksp)
  compileOnly(libs.firebase.crashlytics.plugin)
  compileOnly(libs.firebase.perf.plugin)

}

gradlePlugin {
  /** Register convention plugins so they are available in the build scripts of the application */
  plugins {
    register("androidApplication") {
      id = "plantscan.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidApplicationJacoco") {
      id = "plantscan.android.application.jacoco"
      implementationClass = "AndroidApplicationJacocoConventionPlugin"
    }
    register("androidApplicationCompose"){
      id = "plantscan.android.application.compose"
      implementationClass = "AndroidApplicationComposeConventionPlugin"
    }
    register("androidLibraryCompose") {
      id = "plantscan.android.library.compose"
      implementationClass = "AndroidLibraryComposeConventionPlugin"
    }
    register("androidLibrary"){
      id = "plantscan.android.library"
      implementationClass = "AndroidLibraryConventionPlugin"
    }
    register("androidFeature") {
      id = "plantscan.android.feature"
      implementationClass = "AndroidFeatureConventionPlugin"
    }
    register("androidLibraryJacoco") {
      id = "plantscan.android.library.jacoco"
      implementationClass = "AndroidLibraryJacocoConventionPlugin"
    }
    register("androidTests"){
      id = "plantscan.android.test"
      implementationClass = "AndroidTestConventionPlugin"
    }
    register("androidHilt") {
      id = "plantscan.android.hilt"
      implementationClass = "AndroidHiltConventionPlugin"
    }
    register("androidRoom") {
      id = "plantscan.android.room"
      implementationClass = "AndroidRoomConventionPlugin"
    }
    register("androidFirebase") {
      id = "plantscan.android.application.firebase"
      implementationClass = "AndroidApplicationFirebaseConventionPlugin"
    }
    register("androidFlavors") {
      id = "plantscan.android.application.flavors"
      implementationClass = "AndroidApplicationFlavorsConventionPlugin"
    }
    register("jvmLibrary") {
      id = "plantscan.jvm.library"
      implementationClass = "JvmLibraryConventionPlugin"
    }
  }
}
