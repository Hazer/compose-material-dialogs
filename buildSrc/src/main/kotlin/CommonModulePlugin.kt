import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class CommonModulePlugin: Plugin<Project> {
    private val Project.android: BaseExtension
        get() = extensions.findByName("android") as? BaseExtension
            ?: error("Not an Android module $name")

    override fun apply(project: Project) {
        with(project) {
            applyPlugins()
            androidConf()
            dependenciesConf()
        }
    }

    private fun Project.applyPlugins() {
        plugins.run {
            apply("com.android.library")
            apply("kotlin-android")
            apply("kotlin-android-extensions")
            apply("maven-publish")
            apply("com.jfrog.bintray")
            apply("org.jmailen.kotlinter")
        }
    }

    private fun Project.androidConf() {
        android.run {
            compileSdkVersion(30)

            defaultConfig {
                minSdkVersion(21)
                targetSdkVersion(30)
                versionCode = 1

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildFeatures.compose = true

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            (this as ExtensionAware).configure<KotlinJvmOptions> {
                jvmTarget = "1.8"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            composeOptions {
                kotlinCompilerVersion = Dependencies.Kotlin.version
                kotlinCompilerExtensionVersion = Dependencies.AndroidX.Compose.version
            }
        }
    }

    private fun Project.dependenciesConf() {
        dependencies.apply {
            add("implementation", Dependencies.Kotlin.stdlib)
            add("implementation", Dependencies.AndroidX.coreKtx)
            add("implementation", Dependencies.AndroidX.appcompat)
            add("implementation", Dependencies.material)

            add("implementation", Dependencies.AndroidX.Compose.ui)
            add("implementation", Dependencies.AndroidX.Compose.tooling)
            add("implementation", Dependencies.AndroidX.Compose.material)
            add("implementation", Dependencies.AndroidX.Compose.materialIconsExtended)
        }
    }
}