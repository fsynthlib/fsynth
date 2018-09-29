package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.ValueBounds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class AxisMarkersInterpolationExtrapolationTest {
    @Test
    fun indexOfAxisMarkerRequested() {
        assertEquals(
                actual = computeValueBounds(
                        markers = listOf(
                                AxisMarker(characterIndex = 2, value = 10.0f),
                                AxisMarker(characterIndex = 4, value = 20.0f),
                                AxisMarker(characterIndex = 6, value = 30.0f)),
                        characterIndex = 4),
                expected = ValueBounds(
                        lowerBound = 17.5f,
                        center = 20.0f,
                        upperBound = 22.5f)
        )
    }

    @Test
    fun indexOfNoAxisMarkerRequested() {
        assertEquals(
                actual = computeValueBounds(
                        markers = listOf(
                                AxisMarker(characterIndex = 2, value = 10.0f),
                                AxisMarker(characterIndex = 4, value = 20.0f),
                                AxisMarker(characterIndex = 6, value = 30.0f)),
                        characterIndex = 5),
                expected = ValueBounds(
                        lowerBound = 22.5f,
                        center = 25.0f,
                        upperBound = 27.5f)
        )
    }

    @Test
    fun indexBeforeFirstMarker() {
        assertEquals(
                actual = computeValueBounds(
                        markers = listOf(
                                AxisMarker(characterIndex = 2, value = 10.0f),
                                AxisMarker(characterIndex = 4, value = 20.0f),
                                AxisMarker(characterIndex = 6, value = 80.0f)),
                        characterIndex = 1),
                expected = ValueBounds(
                        lowerBound = 2.5f,
                        center = 5.0f,
                        upperBound = 7.5f)
        )
    }

    @Test
    fun indexAfterLastMarker() {
        assertEquals(
                actual = computeValueBounds(
                        markers = listOf(
                                AxisMarker(characterIndex = 2, value = 10.0f),
                                AxisMarker(characterIndex = 4, value = 20.0f),
                                AxisMarker(characterIndex = 6, value = 80.0f)),
                        characterIndex = 8),
                expected = ValueBounds(
                        lowerBound = 125.0f,
                        center = 140.0f,
                        upperBound = 155.0f)
        )
    }

    @Test
    fun notEnoughMarkers() {
        try {
            computeValueBounds(
                    markers = listOf(
                            AxisMarker(characterIndex = 2, value = 10.0f)),
                    characterIndex = 2)
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(actual = e.message, expected = "There should be at least 2 markers, and 1 found!")
        }
    }

    @Test
    fun negativeCharacterIndex() {
        try {
            computeValueBounds(
                    markers = listOf(
                            AxisMarker(characterIndex = 2, value = 10.0f),
                            AxisMarker(characterIndex = 3, value = 15.0f)),
                    characterIndex = -4)
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(actual = e.message, expected = "Character index should be non-negative!")
        }
    }

    @Test
    fun nonAscendingCharacterIndices() {
        try {
            computeValueBounds(
                    markers = listOf(
                            AxisMarker(characterIndex = 3, value = 10.0f),
                            AxisMarker(characterIndex = 2, value = 15.0f)),
                    characterIndex = 2)
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(
                    actual = e.message,
                    expected = "Given axis markers should have ascending indices (found: 3, 2)!")
        }
    }
}
