package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.RawVisualisation
import it.krzeminski.testutils.plotassert.types.VisualisationRow

/**
 * This module is responsible only for loading whatever was given by the user literally, using the DSL, to the
 * appropriate data structures. Besides ensuring that the DSL has been appropriately used (something that can be
 * validated only in this stage), no deeper validation is done here - further steps are responsible for it.
 */

fun readRawVisualisation(collectVisualisation: PlotConstraintsBuilder.() -> Unit): RawVisualisation {
    val plotConstraintsBuilder = PlotConstraintsBuilder()
    plotConstraintsBuilder.collectVisualisation()
    return plotConstraintsBuilder.build()
}

class PlotConstraintsBuilder(
        private val visualisationRows: MutableList<VisualisationRow> = mutableListOf(),
        private val xAxisMarkerValues: MutableList<Float> = mutableListOf())
{
    fun row(visualisationRowString: String) =
            visualisationRows.add(VisualisationRow(visualisationRowString))

    fun row(yMarkedValue: Float, visualisationRowString: String) =
            visualisationRows.add(VisualisationRow(visualisationRowString, yMarkedValue))

    fun x(vararg xMarkedValuesArg: Float) {
        require(xAxisMarkerValues.isEmpty()) { "X axis markers given more than once!" }
        xAxisMarkerValues.addAll(xMarkedValuesArg.toList())
    }

    fun build(): RawVisualisation =
            RawVisualisation(visualisationRows, xAxisMarkerValues)
}
