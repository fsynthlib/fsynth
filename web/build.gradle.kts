plugins {
    kotlin("js")
}

repositories {
    jcenter()
    maven("https://kotlin.bintray.com/kotlin-js-wrappers")
    maven("https://dl.bintray.com/krzema1212/it.krzeminski")
}

val kotlinVersion: String by rootProject.extra

kotlin {
    target {
        useCommonJs()
        browser {
            webpackTask {
                runTask {
                }
            }
        }
    }

    sourceSets {
        val main by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js:$kotlinVersion")
                implementation("org.jetbrains:kotlin-react:16.13.1-pre.104-kotlin-1.3.72")
                implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.104-kotlin-1.3.72")
                implementation(project(":core"))
                implementation(npm("react", "16.13.1"))
                implementation(npm("react-dom", "16.13.1"))
                implementation(npm("@material-ui/core", "4.9.12"))
                implementation(npm("@material-ui/icons", "4.9.1"))
                implementation(npm("audiobuffer-to-wav", "1.0.0"))
                implementation(npm("wavesurfer.js", "3.3.3"))
            }
        }
        val test by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-js")
                implementation("it.krzeminski:PlotAssert:0.1.0-beta")
            }
        }
    }
}
