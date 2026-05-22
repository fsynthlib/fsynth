package it.krzeminski.fsynth.web.worker.testutils

import it.krzeminski.fsynth.web.worker.self

inline fun measureTimeSeconds(block: () -> Unit): Double {
    val start = self.performance.now()
    block()
    return (self.performance.now() - start) / 1000.0
}
