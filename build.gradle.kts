buildscript {
    val kotlinVersion by extra { "1.3.70" }

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
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
