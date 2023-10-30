pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "plantscan"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core:model")
include(":core:ui")
include(":core:analytics")
include(":core:common")
include(":core:data")
include(":core:data-test")
include(":core:database")
include(":core:datastore")
include(":core:datastore-test")
include(":core:firestore")
include(":core:domain")
include(":core:designsystem")
include(":core:notifications")
include(":feature:camera")
include(":lint")
include(":sync:work")
include(":ui-test-hilt-manifest")
include(":core:testing")
include(":core:network")
include(":feature:web")
include(":feature:history")
include(":feature:findplant")
include(":feature:account")
include(":benchmarks")
include(":app-ps-catalog")
include(":core:auth")
include(":feature:detect")
include(":feature:suggest")
include(":feature:settings")
include(":feature:plant")
