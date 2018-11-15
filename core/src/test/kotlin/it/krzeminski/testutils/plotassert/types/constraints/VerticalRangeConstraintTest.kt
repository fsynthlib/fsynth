package it.krzeminski.testutils.plotassert.types.constraints

import it.krzeminski.testutils.plotassert.exceptions.FailedConstraintException
import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.VisualisationColumn
import kotlin.test.*

class VerticalRangeConstraintTest {
    @Test
    fun assertMatchesWhenInTheMiddleOfRange() {
        VerticalRangeConstraint(minY = 1.0f, maxY = 2.0f)
                .assertMatches(1.5f)
    }

    @Test
    fun assertMatchesWhenOnLowerBoundOfRange() {
        VerticalRangeConstraint(minY = 1.0f, maxY = 2.0f)
                .assertMatches(1.0f)
    }

    @Test
    fun assertMatchesWhenOnUpperBoundOfRange() {
        VerticalRangeConstraint(minY = 1.0f, maxY = 2.0f)
                .assertMatches(2.0f)
    }

    @Test
    fun assertDoesNotMatch() {
        assertFailsWith<FailedConstraintException> {
            VerticalRangeConstraint(minY = 1.0f, maxY = 2.0f)
                    .assertMatches(3.0f)
        }.let { e ->
            assertTrue(e.message in setOf("3.0 is not between 1.0 and 2.0!", "3 is not between 1 and 2!"))
        }
    }

    @Test
    fun singleCapitalICharacterCheckIfMatches() {
        assertTrue(VerticalRangeConstraintBuilder.columnMatchesThisConstraintType(
                VisualisationColumn("   I ")))
    }

    @Test
    fun manySubsequentICharactersCheckIfMatches() {
        assertTrue(VerticalRangeConstraintBuilder.columnMatchesThisConstraintType(
                VisualisationColumn(" III ")))
    }

    @Test
    fun manySubsequentICharactersWithoutSpaceCheckIfMatches() {
        assertTrue(VerticalRangeConstraintBuilder.columnMatchesThisConstraintType(
                VisualisationColumn("IIIII")))
    }

    @Test
    fun capitalICharactersDividedBySpaceCheckIfMatches() {
        assertFalse(VerticalRangeConstraintBuilder.columnMatchesThisConstraintType(
                VisualisationColumn(" I II")))
    }

    @Test
    fun mixedCharactersCheckIfMatches() {
        assertFalse(VerticalRangeConstraintBuilder.columnMatchesThisConstraintType(
                VisualisationColumn(" IIX ")))
    }

    @Test
    fun noCapitalICharacterCheckIfMatches() {
        assertFalse(VerticalRangeConstraintBuilder.columnMatchesThisConstraintType(
                VisualisationColumn("   X ")))
    }

    @Test
    fun singleCapitalICharacterBuildConstraint() {
        assertEquals(
                actual = VerticalRangeConstraintBuilder.buildConstraintFromColumn(
                        column = VisualisationColumn("   I "),
                        yAxisMarkers = listOf(AxisMarker(5.0f, 0), AxisMarker(1.0f, 4))),
                expected = VerticalRangeConstraint(minY = 1.5f, maxY = 2.5f))
    }

    @Test
    fun manySubsequentICharactersBuildConstraint() {
        assertEquals(
                actual = VerticalRangeConstraintBuilder.buildConstraintFromColumn(
                        column = VisualisationColumn(" III "),
                        yAxisMarkers = listOf(AxisMarker(5.0f, 0), AxisMarker(1.0f, 4))),
                expected = VerticalRangeConstraint(minY = 1.5f, maxY = 4.5f))
    }

    @Test
    fun manySubsequentICharactersWithoutSpaceBuildConstraint() {
        assertEquals(
                actual = VerticalRangeConstraintBuilder.buildConstraintFromColumn(
                        column = VisualisationColumn("IIIII"),
                        yAxisMarkers = listOf(AxisMarker(5.0f, 0), AxisMarker(1.0f, 4))),
                expected = VerticalRangeConstraint(minY = 0.5f, maxY = 5.5f))
    }
}
