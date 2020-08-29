rootProject.name = "fsynth"

include("core", "cli", "web", ":web:worker", ":web:serviceworker", "android")

enableFeaturePreview("GRADLE_METADATA")
