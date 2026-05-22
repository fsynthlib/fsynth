#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.7.0")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("actions:setup-java:v4")
@file:DependsOn("android-actions:setup-android:v3")
@file:DependsOn("codecov:codecov-action:v4")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.androidactions.SetupAndroid_Untyped
import io.github.typesafegithub.workflows.actions.codecov.CodecovAction
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow

workflow(
    name = "CI",
    on = listOf(
        Push(branches = listOf("master")),
        PullRequest(branches = listOf("master")),
    ),
    sourceFile = __FILE__,
) {
    job(
        id = "core",
        runsOn = UbuntuLatest,
    ) {
        uses(name = "Check out", action = Checkout())
        uses(
            name = "Set up JDK 8",
            action = SetupJava(
                javaVersion = "8",
                distribution = SetupJava.Distribution.Temurin,
            ),
        )
        run(name = "Build core", command = "./gradlew :core:build")
    }

    job(
        id = "android",
        runsOn = UbuntuLatest,
    ) {
        uses(name = "Check out", action = Checkout())
        uses(
            name = "Set up JDK 17",
            action = SetupJava(
                javaVersion = "17",
                distribution = SetupJava.Distribution.Temurin,
            ),
        )
        uses(
            name = "Set up Android SDK",
            action = SetupAndroid_Untyped(),
        )
        uses(
            name = "Set up JDK 8",
            action = SetupJava(
                javaVersion = "8",
                distribution = SetupJava.Distribution.Temurin,
            ),
        )
        run(name = "Build android", command = "./gradlew :android:build")
    }

    job(
        id = "cli",
        runsOn = UbuntuLatest,
    ) {
        uses(name = "Check out", action = Checkout())
        uses(
            name = "Set up JDK 8",
            action = SetupJava(
                javaVersion = "8",
                distribution = SetupJava.Distribution.Temurin,
            ),
        )
        run(name = "Build CLI", command = "./gradlew :cli:build")
        uses(
            name = "Upload coverage to Codecov",
            action = CodecovAction(),
        )
    }

    job(
        id = "web-e2e",
        runsOn = UbuntuLatest,
    ) {
        uses(name = "Check out", action = Checkout())
        uses(
            name = "Set up JDK 17",
            action = SetupJava(
                javaVersion = "17",
                distribution = SetupJava.Distribution.Temurin,
            ),
        )
        run(name = "E2E smoke test", command = "./gradlew :web-e2e:test")
    }

    job(
        id = "web",
        runsOn = UbuntuLatest,
        permissions = mapOf(Permission.Contents to Mode.Write),
    ) {
        uses(name = "Check out", action = Checkout())
        uses(
            name = "Set up JDK 8",
            action = SetupJava(
                javaVersion = "8",
                distribution = SetupJava.Distribution.Temurin,
            ),
        )
        run(name = "Build web", command = "./gradlew :web:build")
        run(
            name = "Deploy to GitHub Pages",
            condition = expr("github.ref == 'refs/heads/master' && github.event_name == 'push'"),
            env = mapOf(
                "GITHUB_TOKEN" to expr("secrets.GITHUB_TOKEN"),
            ),
            command = """
                set -e
                git config --global user.email "actions@github.com"
                git config --global user.name "GitHub Actions"
                git clone --single-branch --branch gh-pages \
                  https://x-access-token:${'$'}GITHUB_TOKEN@github.com/${'$'}GITHUB_REPOSITORY.git gh-pages
                cd gh-pages
                git checkout --orphan gh-pages-new
                git rm -rf .
                cd ..
                cp -av web/build/distributions gh-pages
                cd gh-pages
                mv distributions/* .
                git add -A
                git commit -m "Deploy to GitHub Pages: ${'$'}GITHUB_SHA" --allow-empty
                git branch -D gh-pages 2>/dev/null || true
                git branch -m gh-pages-new gh-pages
                git push --force origin gh-pages
            """.trimIndent(),
        )
    }
}
