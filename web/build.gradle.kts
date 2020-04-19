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
                implementation("org.jetbrains:kotlin-react:16.6.0-pre.68-kotlin-1.3.20")
                implementation("org.jetbrains:kotlin-react-dom:16.6.0-pre.68-kotlin-1.3.20")
                implementation(project(":core"))
                implementation(npm("react", "16.6.0"))
                implementation(npm("react-dom", "16.6.0"))
                implementation(npm("@material-ui/core", "3.3.2"))
                implementation(npm("@material-ui/icons", "3.0.1"))
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

val copyFilesToDeploy = tasks.register("copyFilesToDeploy", Copy::class) {
    from(file("src/main/resources/index.html"), file("$buildDir/distributions/web.js"))
    into(file("$buildDir/deploy-me"))
}

tasks.getByName("assemble").dependsOn(copyFilesToDeploy)
