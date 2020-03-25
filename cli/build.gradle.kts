plugins {
    kotlin("jvm")
    application
    jacoco
}

repositories {
    maven("https://dl.bintray.com/krzema1212/it.krzeminski")
}

val kotlinVersion: String by rootProject.extra

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("com.github.ajalt:clikt:1.7.0")
    implementation(project(":core"))

    testImplementation("junit:junit:4.12")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("it.krzeminski:PlotAssert:0.1.0-beta")
}

application {
    mainClassName = "it.krzeminski.fsynth.ConsoleEntryPointKt"
    applicationName = "fsynth"
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"

jacoco {
    toolVersion = "0.8.3"
}

val jacocoTestReport = tasks.getByName<JacocoReport>("jacocoTestReport") {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }

    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                        // Excluding songs and instruments from code coverage.
                        // Rationale: testing songs would be like testing MIDI files - some pieces of data.
                        // Regarding instruments, they are defined as math operations on simpler waveforms and testing them
                        // would bring no benefit, however operations alone (addition, multiplication) are still tested.
                        "it/krzeminski/fsynth/songs/**",
                        "it/krzeminski/fsynth/instruments/**")
            }
        }))
    }
}

tasks.getByName("check").dependsOn(jacocoTestReport)
