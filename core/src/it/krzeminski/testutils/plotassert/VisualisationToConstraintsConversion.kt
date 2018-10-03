package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.RawVisualisation
import it.krzeminski.testutils.plotassert.types.VisualisationColumn
import it.krzeminski.testutils.plotassert.types.constraints.Constraint

fun RawVisualisation.toConstraints(): List<Constraint> {
    validateAllStringsHaveTheSameLength(this)

    val xAxisMarkers = readXAxisMarkers(this)
    val yAxisMarkers = readYAxisMarkers(this)

    return this.columns.mapIndexedNotNull { xIndex, visualisationColumn ->
        buildConstraint(visualisationColumn, yAxisMarkers, xAxisMarkers, xIndex)
    }
}

private fun validateAllStringsHaveTheSameLength(rawVisualisation: RawVisualisation) {
    val firstRowLength = rawVisualisation.visualisationRows.first().characters.length
    val allRowsHaveTheSameLength = rawVisualisation.visualisationRows
            .map { it.characters.length }
            .all { it == firstRowLength }
    val xAxisMarkersStringHasTheSameLengthAsRows = rawVisualisation.xAxis?.markers?.length == firstRowLength

    require(allRowsHaveTheSameLength && xAxisMarkersStringHasTheSameLengthAsRows) {
        "Visualisation rows and the X axis markers string must have the same length!"
    }
}

private fun buildConstraint(visualisationColumn: VisualisationColumn,
                            yAxisMarkers: List<AxisMarker>,
                            xAxisMarkers: List<AxisMarker>,
                            xIndex: Int): Constraint?
{
    val yValueConstraint = mapVisualisationColumnToConstraint(visualisationColumn, yAxisMarkers)
    val xValueBounds = computeValueBounds(xAxisMarkers, xIndex)
    return yValueConstraint?.let {
        Constraint(x = xValueBounds.center, yValueConstraint = yValueConstraint)
    }
}

private val RawVisualisation.columns: List<VisualisationColumn>
        get() {
            return visualisationRows.first().characters.mapIndexed { index, _ ->
                VisualisationColumn(visualisationRows
                        .map { it.characters[index] }
                        .joinToString(separator = ""))
            }
        }
