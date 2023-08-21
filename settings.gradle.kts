pluginManagement {
  includeBuild("build-logic")
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "plantscan"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core:data")
include(":core:model")
include(":core:testing")
include(":core:domain")
include(":core:database")
include(":core:data-test")
include(":feature:camera")
include(":core:ui")
include(":core:designsystem")
include(":core:common")
include(":core:firestore")
include(":feature:web")
include(":core:crashlytics")

include(":feature:splash")
include(":feature:home")
include(":feature:detect")
include(":feature:detail")
include(":feature:mygarden")
include(":feature:settings")
include(":feature:login")
include(":feature:signup")
include(":feature:category")
include(":feature:list")
include(":ui-test-hilt-manifest")
