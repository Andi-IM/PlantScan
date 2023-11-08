import com.github.andiim.plantscan.app.FlavorDimension
import com.github.andiim.plantscan.app.PsFlavor

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.application.compose)
}

android {
    defaultConfig {
        applicationId = "com.github.andiim.pscatalog"
        versionCode = 1
        versionName = "1.0"

        // The UI catalog does not depend on content from the app, however, it depends on modules
        // which do, so we must specify a default value for the contentType dimension.
        missingDimensionStrategy(FlavorDimension.contentType.name, PsFlavor.demo.name)
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    buildTypes {
        release {
            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    namespace = "com.github.andiim.pscatalog"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    implementation(libs.compose.activity)
}