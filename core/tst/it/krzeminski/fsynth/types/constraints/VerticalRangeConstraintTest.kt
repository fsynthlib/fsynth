package it.krzeminski.fsynth.types.constraints

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.VisualisationColumn
import it.krzeminski.testutils.plotassert.types.constraints.VerticalRangeConstraint
import it.krzeminski.testutils.plotassert.types.constraints.VerticalRangeConstraintBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VerticalRangeConstraintTest {
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
}
