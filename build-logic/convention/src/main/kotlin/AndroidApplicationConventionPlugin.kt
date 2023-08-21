import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.github.andiim.plantscan.app.configureKotlinAndroid
import com.github.andiim.plantscan.app.configurePrintApksTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
        apply("org.jetbrains.kotlin.plugin.parcelize")
      }

      extensions.configure<ApplicationExtension> {
        configureKotlinAndroid(commonExtension = this)
        defaultConfig.targetSdk = 33
      }
      extensions.configure<ApplicationAndroidComponentsExtension> { configurePrintApksTask(this) }
    }
  }
}
