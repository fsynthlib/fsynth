buildscript {
    val kotlinVersion by extra { "1.3.70" }

    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        // As a workaround, the root project knows about the Android project.
        // Otherwise, there are some issues with building the Android project.
        classpath("com.android.tools.build:gradle:3.5.0")
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-progressive"
        }
    }
}

subprojects {
    apply(from = "$rootDir/ktlint.gradle.kts")
}
