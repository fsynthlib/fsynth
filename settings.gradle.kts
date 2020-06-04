rootProject.name = "fsynth"

include("core", "cli", "web", ":web:worker", "android")

enableFeaturePreview("GRADLE_METADATA")
