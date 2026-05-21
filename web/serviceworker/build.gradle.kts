import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

plugins {
    kotlin("multiplatform")
}

kotlin {
    js {
        browser {
            webpackTask {
                output.libraryTarget = Target.SELF
            }
        }
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
            }
        }
    }
}
