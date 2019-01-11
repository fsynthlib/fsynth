package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.RawVisualisation
import it.krzeminski.testutils.plotassert.types.RawXAxis
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
        private var rawXAxis: RawXAxis? = null)
{
    fun row(visualisationRowString: String) =
            visualisationRows.add(VisualisationRow(visualisationRowString))

    fun row(yMarkedValue: Float, visualisationRowString: String) =
            visualisationRows.add(VisualisationRow(visualisationRowString, yMarkedValue))

    fun build(): RawVisualisation {
        val rawXAxisFinal = rawXAxis
        requireNotNull(rawXAxisFinal) { "X axis not given!" }
        return RawVisualisation(visualisationRows, rawXAxisFinal)
    }

    fun xAxis(collectXAxisDefinition: RawXAxisBuilder.() -> Unit) {
        require(rawXAxis == null) { "X axis given more than once!" }
        rawXAxis = with(RawXAxisBuilder()) {
            collectXAxisDefinition()
            build()
        }
    }
}

class RawXAxisBuilder(
        private var markers: String? = null,
        private val values: MutableList<Float> = mutableListOf())
{
    fun markers(markersString: String) {
        require(markers == null) { "X axis markers given more than once!" }
        markers = markersString
    }

    fun values(vararg xAxisValues: Float) {
        require(values.isEmpty()) { "X axis values given more than once!" }
        values.addAll(xAxisValues.toList())
    }

    fun build(): RawXAxis {
        val markersFinal = markers
        requireNotNull(markersFinal) { "X axis markers not given!" }
        require(values.size > 0) { "X axis values not given!" }
        return RawXAxis(markersFinal, values)
    }
}
