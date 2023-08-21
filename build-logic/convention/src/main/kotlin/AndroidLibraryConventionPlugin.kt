import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.github.andiim.plantscan.app.configureFlavors
import com.github.andiim.plantscan.app.configureKotlinAndroid
import com.github.andiim.plantscan.app.configurePrintApksTask
import com.github.andiim.plantscan.app.disableUnnecessaryAndroidTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            with(pluginManager){
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.parcelize")
            }

            extensions.configure<LibraryExtension>(){
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 33
                configureFlavors(this)
            }
            extensions.configure<LibraryAndroidComponentsExtension>(){
                configurePrintApksTask(this)
                disableUnnecessaryAndroidTest(target)
            }

            dependencies {
                add("androidTestImplementation",kotlin("test"))
                add("testImplementation",kotlin("test"))
            }
        }
    }
}