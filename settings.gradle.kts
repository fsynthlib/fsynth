pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

rootProject.name = "fsynth"

include("core", "cli", "web", ":web:worker", ":web:serviceworker", "android", "web-e2e")
