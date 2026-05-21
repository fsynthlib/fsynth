import org.ajoberstar.grgit.Grgit

plugins {
    kotlin("multiplatform")
    id("org.ajoberstar.grgit") version "5.3.2"
}

val generatedDir = layout.buildDirectory.dir("generated-src").get().asFile

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
            kotlin.srcDirs(kotlin.srcDirs, "$generatedDir/")
        }
        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
                implementation("it.krzeminski.vis-assert:vis-assert:0.4.1-beta")
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
    val grgit = Grgit.open(mapOf("currentDir" to project.rootDir))
    val fileDirectory = "$generatedDir/it/krzeminski/fsynth/generated"
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

tasks.configureEach {
    if (name.startsWith("compileKotlin")) {
        dependsOn(generateGitInfo)
    }
}

tasks.named<org.gradle.jvm.tasks.Jar>("metadataSourcesJar") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
