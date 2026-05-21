buildscript {
    val kotlinVersion by extra { "2.3.21" }

    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.android.tools.build:gradle:9.2.0")
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.add("-progressive")
        }
    }


}

subprojects {
    apply(from = "$rootDir/ktlint.gradle.kts")
}
