package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.VisualisationColumn
import it.krzeminski.testutils.plotassert.types.constraints.YValueConstraint
import it.krzeminski.testutils.plotassert.types.constraints.ConstraintBuilder
import kotlin.test.*

class VisualisationColumnToConstraintMappingTest {
    @Test
    fun oneConstraintMatches() {
        val constraintToReturn = object : YValueConstraint() { }
        val mockConstraintBuilder1 = object : ConstraintBuilder() {
            override fun columnMatchesThisConstraintType(column: VisualisationColumn) = true

            override fun buildConstraintFromColumn(column: VisualisationColumn, yAxisMarkers: List<AxisMarker>) =
                    constraintToReturn
        }
        val mockConstraintBuilder2 = object : ConstraintBuilder() {
            override fun columnMatchesThisConstraintType(column: VisualisationColumn) = false

            override fun buildConstraintFromColumn(column: VisualisationColumn, yAxisMarkers: List<AxisMarker>): YValueConstraint {
                throw IllegalStateException("This method is irrelevant for this unit test.")
            }
        }
        val mockGetAvailableConstraintBuilders = { listOf(mockConstraintBuilder1, mockConstraintBuilder2) }

        assertEquals(
                actual = mapVisualisationColumnToConstraint(
                        VisualisationColumn("MOCK VISUALISATION COLUMN"),
                        listOf(AxisMarker(5.0f, 0), AxisMarker(1.0f, 4)),
                        mockGetAvailableConstraintBuilders),
                expected = constraintToReturn)
    }

    @Test
    fun noConstraintDesired() {
        assertEquals(
                actual = mapVisualisationColumnToConstraint(
                        VisualisationColumn("     "),
                        listOf(AxisMarker(5.0f, 0), AxisMarker(1.0f, 4))),
                expected = null
        )
    }

    @Test
    fun noConstraintsMatch() {
        val mockConstraintBuilder1 = object : ConstraintBuilder() {
            override fun columnMatchesThisConstraintType(column: VisualisationColumn) = false

            override fun buildConstraintFromColumn(column: VisualisationColumn, yAxisMarkers: List<AxisMarker>): YValueConstraint {
                throw IllegalStateException("This method shouldn't be used  for this unit test.")
            }
        }
        val mockConstraintBuilder2 = object : ConstraintBuilder() {
            override fun columnMatchesThisConstraintType(column: VisualisationColumn) = false

            override fun buildConstraintFromColumn(column: VisualisationColumn, yAxisMarkers: List<AxisMarker>): YValueConstraint {
                throw IllegalStateException("This method shouldn't be used  for this unit test.")
            }
        }
        val mockGetAvailableConstraintBuilders = { listOf(mockConstraintBuilder1, mockConstraintBuilder2) }
        try {
            mapVisualisationColumnToConstraint(
                    VisualisationColumn("MOCK VISUALISATION COLUMN"),
                    listOf(AxisMarker(5.0f, 0), AxisMarker(1.0f, 4)),
                    mockGetAvailableConstraintBuilders)
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: Exception) {
            assertEquals("No constraints match this visualisation column: MOCK VISUALISATION COLUMN", e.message)
        }
    }

    @Test
    fun multipleConstraintsMatch() {
        val mockConstraintBuilder1 = object : ConstraintBuilder() {
            override fun columnMatchesThisConstraintType(column: VisualisationColumn) = true

            override fun buildConstraintFromColumn(column: VisualisationColumn, yAxisMarkers: List<AxisMarker>): YValueConstraint {
                throw IllegalStateException("This method shouldn't be used  for this unit test.")
            }
        }
        val mockConstraintBuilder2 = object : ConstraintBuilder() {
            override fun columnMatchesThisConstraintType(column: VisualisationColumn) = true

            override fun buildConstraintFromColumn(column: VisualisationColumn, yAxisMarkers: List<AxisMarker>): YValueConstraint {
                throw IllegalStateException("This method shouldn't be used  for this unit test.")
            }
        }
        val mockGetAvailableConstraintBuilders = { listOf(mockConstraintBuilder1, mockConstraintBuilder2) }
        try {
            mapVisualisationColumnToConstraint(
                    VisualisationColumn("MOCK VISUALISATION COLUMN"),
                    listOf(AxisMarker(5.0f, 0), AxisMarker(1.0f, 4)),
                    mockGetAvailableConstraintBuilders)
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: Exception) {
            assertNotNull(e.message)
            assertTrue(e.message!!.startsWith("Ambiguous constraint; more than 1 constraint type matches:"))
        }
    }
}
