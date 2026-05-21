plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

val reactVersion = "19.2.5"

kotlin {
    js {
        useCommonJs()
        browser {
            webpackTask {
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
                implementation(npm("audiobuffer-to-wav", "1.0.0"))
                implementation(npm("wavesurfer.js", "7.12.7"))
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
    from("worker/build/distributions")
    into("$buildDir/distributions")
    dependsOn(":web:worker:build")
}

val copyServiceWorkerDistributionFiles = tasks.register<Copy>("copyServiceWorkerDistributionFiles") {
    from("serviceworker/build/distributions")
    into("$buildDir/distributions")
    dependsOn(":web:serviceworker:build")
}

tasks.named("assemble") {
    dependsOn(copyWorkerDistributionFiles)
    dependsOn(copyServiceWorkerDistributionFiles)
}
