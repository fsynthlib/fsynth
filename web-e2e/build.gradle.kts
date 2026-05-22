plugins {
    kotlin("jvm")
}

val kotlinVersion: String by rootProject.extra

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.15.0")
    testImplementation("io.github.bonigarcia:webdrivermanager:5.6.2")
    testImplementation("ru.yandex.qatools.ashot:ashot:1.5.4")
}

tasks.test {
    dependsOn(":web:build")
    systemProperty("web.dist", rootProject.projectDir.resolve("web/build/distributions").absolutePath)
}
