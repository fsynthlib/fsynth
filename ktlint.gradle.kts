repositories {
    jcenter()
}

val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.32.0")
}

val ktlintTask = tasks.register<JavaExec>("ktlint") {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args("src/**/*.kt", "-v")
}

tasks.register<JavaExec>("ktlintFormat") {
    group = "formatting"
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args("-F", "src/**/*.kt")
}

afterEvaluate {
    project.tasks["check"].dependsOn(ktlintTask)
}
