plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

val reactVersion = "19.2.5"

kotlin {
    js {
        binaries.executable()
        useCommonJs()
        browser {
            webpackTask {
            }
            distribution {
                outputDirectory = file("$projectDir/build/distributions")
            }
        }
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:2026.4.14-19.2.5")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:2026.4.14-19.2.5")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-mui-material:2026.5.1-5.18.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
                implementation(project(":core"))
                implementation(npm("react", reactVersion))
                implementation(npm("react-dom", reactVersion))
                implementation(npm("@mui/material", "5.18.0"))
                implementation(npm("@emotion/react", "11.13.5"))
                implementation(npm("@emotion/styled", "11.13.5"))
                implementation(npm("audiobuffer-to-wav", "1.0.0"))
                implementation(npm("wavesurfer.js", "7.12.7"))
                implementation(devNpm("html-webpack-plugin", "5.6.3"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

val copyWorkerDistributionFiles = tasks.register<Copy>("copyWorkerDistributionFiles") {
    from(project(":web:worker").layout.buildDirectory.dir("distributions"))
    into(layout.buildDirectory.dir("distributions"))
    dependsOn(":web:worker:build")
}

val copyServiceWorkerDistributionFiles = tasks.register<Copy>("copyServiceWorkerDistributionFiles") {
    from(project(":web:serviceworker").layout.buildDirectory.dir("distributions"))
    into(layout.buildDirectory.dir("distributions"))
    dependsOn(":web:serviceworker:build")
}

tasks.named("jsBrowserDistribution") {
    dependsOn(copyWorkerDistributionFiles)
    dependsOn(copyServiceWorkerDistributionFiles)
}

tasks.named<Sync>("jsBrowserDistribution") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}


