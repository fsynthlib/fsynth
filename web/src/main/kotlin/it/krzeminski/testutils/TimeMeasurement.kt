package it.krzeminski.testutils

import kotlin.browser.window

inline fun measureTimeSeconds(block: () -> Unit): Double {
    val start = window.performance.now()
    block()
    return (window.performance.now() - start) / 1000.0
}
