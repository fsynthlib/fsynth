package it.krzeminski.fsynth.testutils

import it.krzeminski.fsynth.types.BoundedWaveform
import kotlin.test.assertEquals

fun assertValuesEqual(expected: BoundedWaveform, actual: BoundedWaveform, delta: Float) {
    assertEquals(expected.duration, actual.duration)
    val numberOfSamples = (expected.duration / delta).toInt()
    (0..numberOfSamples).asSequence()
            .forEach {
                val t = it.toFloat() * delta
                val expectedValue = expected.waveform(t)
                val actualValue = actual.waveform(t)
                assertEquals(
                        expected = expectedValue,
                        actual = actualValue,
                        message = "Value mismatch for argument $t: expected $expectedValue, actual $actualValue")
            }
}
