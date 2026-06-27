import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.github.andiim.plantscan.app.configureKotlinAndroid
import com.github.andiim.plantscan.app.configurePrintApksTask
import com.github.andiim.plantscan.app.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
            }
            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                // defaultConfig.targetSdk = 33
                defaultConfig.targetSdk =
                    libs.findVersion("target.sdk.version").get().toString().toInt()
            }

            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
            }
        }

    }
}