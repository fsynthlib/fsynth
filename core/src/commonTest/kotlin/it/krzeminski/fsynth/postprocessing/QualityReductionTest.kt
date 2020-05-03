package it.krzeminski.fsynth.postprocessing

import kotlin.test.Test
import kotlin.test.assertEquals

class QualityReductionTest {
    @Test
    fun toTwoLevels() {
        listOf(
                Pair(-1.0f, -1.0f),
                Pair(-0.75f, -1.0f),
                Pair(-0.1f, -1.0f),
                Pair(0.1f, 1.0f),
                Pair(0.75f, 1.0f),
                Pair(1.0f, 1.0f)).forEach { (before, after) ->
            assertEquals(after, before.reduceLevelsPerSample(2))
        }
    }

    @Test
    fun toThreeLevels() {
        listOf(
                Pair(-1.0f, -1.0f),
                Pair(-0.75f, -1.0f),
                Pair(-0.1f, 0.0f),
                Pair(0.1f, 0.0f),
                Pair(0.75f, 1.0f),
                Pair(1.0f, 1.0f)).forEach { (before, after) ->
            assertEquals(after, before.reduceLevelsPerSample(3))
        }
    }
}
