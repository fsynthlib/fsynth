plugins {
    kotlin("jvm")
    application
    jacoco
}


val kotlinVersion: String by rootProject.extra

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("com.github.ajalt.clikt:clikt:5.1.0")
    implementation(project(":core"))

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClass = "it.krzeminski.fsynth.ConsoleEntryPointKt"
    applicationName = "fsynth"
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_17
}

jacoco {
    toolVersion = "0.8.14"
}

val jacocoTestReport = tasks.getByName<JacocoReport>("jacocoTestReport") {
    reports {
        xml.required.set(true)
        html.required.set(true)
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

tasks.withType<Test>().configureEach {
    failOnNoDiscoveredTests = false
}
