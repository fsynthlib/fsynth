package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.RawVisualisation
import it.krzeminski.testutils.plotassert.types.VisualisationRow

/**
 * Returns Y axis markers in the order they were given, from top to bottom, so that it's easier to find the right Y axis
 * value when an arbitrary character index is given.
 */
fun readYAxisMarkers(rawVisualisation: RawVisualisation): List<AxisMarker> {
    validate(rawVisualisation)

    return rawVisualisation.visualisationRows
            .mapIndexedNotNull { index, visualisationRow ->
                visualisationRow.yAxisMarkerValue?.let {
                    AxisMarker(
                            value = it,
                            characterIndex = index)
                }
            }
}

private fun validate(rawVisualisation: RawVisualisation) {
    validateIfAtLeastTwoMarkers(rawVisualisation.visualisationRows)
    validateIfMarkerValuesIncreaseMonotonically(rawVisualisation.visualisationRows)
}

private fun validateIfAtLeastTwoMarkers(visualisationRows: List<VisualisationRow>) {
    val numberOfMarkers = visualisationRows.mapNotNull { it.yAxisMarkerValue }.count()
    require(numberOfMarkers >= 2) {
        "$numberOfMarkers Y axis marker(s) found, and there should be at least two!" }
}

private fun validateIfMarkerValuesIncreaseMonotonically(visualisationRows: List<VisualisationRow>) {
    visualisationRows
            .mapNotNull { it.yAxisMarkerValue }
            .zipWithNext { a, b -> Pair(a, b) }
            .forEach { pair ->
                require(pair.first - pair.second > 0.0f) {
                    "Given Y axis markers should have descending values " +
                            "(found: ${pair.first}, ${pair.second})!"
                }
            }
}
