import org.ajoberstar.grgit.Grgit

plugins {
    kotlin("multiplatform")
    id("org.ajoberstar.grgit") version "3.0.0"
}

repositories {
    maven("https://dl.bintray.com/krzema1212/it.krzeminski")
}

kotlin {
    jvm {
    }
    js {
        browser()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
            }
            kotlin.srcDirs(kotlin.srcDirs, "$buildDir/generated/")
        }
        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
                implementation("it.krzeminski:PlotAssert:0.1.0-beta")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("org.jetbrains.kotlin:kotlin-test-junit")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-js")
            }
        }
    }
}

val generateGitInfo = tasks.register("generateGitInfo") {
    val grgit = Grgit.open()
    val fileDirectory = "$buildDir/generated/it/krzeminski/fsynth/generated"
    val fileName = "GitInfo.kt"

    val sha1 = grgit.head().id
    val timeUnixTimestamp = grgit.head().dateTime.toEpochSecond()
    val containsUncommittedChanges = !grgit.status().isClean

    val contents = """
        package it.krzeminski.fsynth.generated

        import it.krzeminski.gitinfo.types.CommitMetadata
        import it.krzeminski.gitinfo.types.GitInfo

        val gitInfo = GitInfo(
                latestCommit = CommitMetadata(
                        sha1 = "$sha1",
                        timeUnixTimestamp = $timeUnixTimestamp),
                containsUncommittedChanges = $containsUncommittedChanges)
        """.trimIndent()
    doLast {
        file(fileDirectory).mkdirs()
        file("$fileDirectory/$fileName").writeText(contents)
    }
}

// There's no easy way to express that 'generateGitInfo' task needs to be executed before any code is compiled.
// That's why a dependency on this task is added to all known tasks that deal with compiling code, so that this file
// already exists when code for any platform is about to be compiled.
tasks.getByName("compileKotlinJs").dependsOn(generateGitInfo)
tasks.getByName("compileKotlinJvm").dependsOn(generateGitInfo)
tasks.getByName("compileKotlinMetadata").dependsOn(generateGitInfo)
