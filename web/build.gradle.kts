import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    kotlin("js")
}

repositories {
    jcenter()
    maven("https://kotlin.bintray.com/kotlin-js-wrappers")
    maven("https://dl.bintray.com/cfraser/muirwik")
}

// The below versions cannot be freely changed independently. Only certain combinations are valid and map to the actual
// existing versions in the repositories.
val kotlinVersion: String by rootProject.extra
val reactVersion = "17.0.0"
val jsWrappersVersion = "pre.133"

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
                implementation("org.jetbrains:kotlin-react:$reactVersion-$jsWrappersVersion-kotlin-$kotlinVersion")
                implementation("org.jetbrains:kotlin-react-dom:$reactVersion-$jsWrappersVersion-kotlin-$kotlinVersion")
                implementation("org.jetbrains:kotlin-styled:5.2.0-$jsWrappersVersion-kotlin-$kotlinVersion")
                implementation("org.jetbrains:kotlin-css-js:1.0.0-$jsWrappersVersion-kotlin-$kotlinVersion")
                implementation("com.ccfraser.muirwik:muirwik-components:0.6.5")
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
