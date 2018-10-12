package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.ValueBounds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
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
    fun ascendingIndicesButDescendingValues() {
        assertEquals(
                actual = computeValueBounds(
                        markers = listOf(
                                AxisMarker(characterIndex = 0, value = 5.0f),
                                AxisMarker(characterIndex = 4, value = 1.0f)),
                        characterIndex = 1),
                expected = ValueBounds(
                        lowerBound = 3.5f,
                        center = 4.0f,
                        upperBound = 4.5f)
        )
    }

    @Test
    fun notEnoughMarkers() {
        assertFailsWith<IllegalArgumentException> {
            computeValueBounds(
                    markers = listOf(
                            AxisMarker(characterIndex = 2, value = 10.0f)),
                    characterIndex = 2)
        }.let { e ->
            assertEquals("There should be at least 2 markers, and 1 found!", e.message)
        }
    }

    @Test
    fun negativeCharacterIndex() {
        assertFailsWith<IllegalArgumentException> {
            computeValueBounds(
                    markers = listOf(
                            AxisMarker(characterIndex = 2, value = 10.0f),
                            AxisMarker(characterIndex = 3, value = 15.0f)),
                    characterIndex = -4)
        }.let { e ->
            assertEquals("Character index should be non-negative!", e.message)
        }
    }

    @Test
    fun nonAscendingCharacterIndices() {
        assertFailsWith<IllegalArgumentException> {
            computeValueBounds(
                    markers = listOf(
                            AxisMarker(characterIndex = 3, value = 10.0f),
                            AxisMarker(characterIndex = 2, value = 15.0f)),
                    characterIndex = 2)
        }.let { e ->
            assertEquals("Given axis markers should have ascending indices (found: 3, 2)!", e.message)
        }
    }
}
