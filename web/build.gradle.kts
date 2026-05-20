import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    kotlin("js")
}

repositories {
    mavenCentral()
}

// The below versions cannot be freely changed independently. Only certain combinations are valid and map to the actual
// existing versions in the repositories.
val kotlinVersion: String by rootProject.extra
val reactVersion = "17.0.2"
val jsWrappersVersion = "pre.204"

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
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$reactVersion-$jsWrappersVersion-kotlin-$kotlinVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$reactVersion-$jsWrappersVersion-kotlin-$kotlinVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.0-$jsWrappersVersion-kotlin-$kotlinVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css-js:1.0.0-$jsWrappersVersion-kotlin-$kotlinVersion")
                implementation("com.ccfraser.muirwik:muirwik-components:0.7.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
                implementation(project(":core"))
                implementation(npm("react", reactVersion))
                implementation(npm("react-dom", reactVersion))
                implementation(npm("@material-ui/core", "4.11.0"))
                implementation(npm("audiobuffer-to-wav", "1.0.0"))
                implementation(npm("wavesurfer.js", "3.3.3"))
            }
        }
        val test by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-js")
            }
        }
    }
}

val copyWorkerDistributionFiles = tasks.register("copyWorkerDistributionFiles", Copy::class) {
    from("worker/build/distributions")
    into("$buildDir/distributions")
}.dependsOn(":web:worker:build")

val copyServiceWorkerDistributionFiles = tasks.register("copyServiceWorkerDistributionFiles", Copy::class) {
    from("serviceworker/build/distributions")
    into("$buildDir/distributions")
}.dependsOn(":web:serviceworker:build")

tasks.named("assemble").dependsOn(copyWorkerDistributionFiles)
tasks.named("assemble").dependsOn(copyServiceWorkerDistributionFiles)
