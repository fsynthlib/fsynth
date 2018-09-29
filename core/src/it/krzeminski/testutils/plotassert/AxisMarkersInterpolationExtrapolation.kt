package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.ValueBounds

/**
 * Assuming some grid of characters and axis markers (doesn't matter if for X or Y axis), calculates what actual values
 * correspond to the given [characterIndex].
 *
 * @param markers Axis markers sorted ascending, according to [AxisMarker.characterIndex].
 * @param characterIndex Zero-based index of the character, counting from left (X axis) or top (Y axis).
 * @return Specifies what's the value in the middle and on both ends of the character's range.
 */
fun computeValueBounds(markers: List<AxisMarker>, characterIndex: Int): ValueBounds {
    validate(markers, characterIndex)

    val interpolateAroundIndex = { shift: Float ->
        interpolate(markers, characterIndex.toFloat() + shift)
    }
    val extrapolateAroundIndex = { shift: Float ->
        extrapolate(markers, characterIndex.toFloat() + shift)
    }
    val valueAroundIndex = { shift: Float ->
        interpolateAroundIndex(shift) ?: extrapolateAroundIndex(shift)
    }
    return ValueBounds(
            lowerBound = valueAroundIndex(-0.5f),
            center = valueAroundIndex(0.0f),
            upperBound = valueAroundIndex(0.5f))
}

private fun validate(markers: List<AxisMarker>, characterIndex: Int) {
    require(markers.size >= 2) { "There should be at least 2 markers, and ${markers.size} found!" }
    require(characterIndex >= 0) { "Character index should be non-negative!" }
    validateIfMarkerCharacterIndicesIncreaseMonotonically(markers)
}

private fun validateIfMarkerCharacterIndicesIncreaseMonotonically(axisMarkers: List<AxisMarker>) {
    axisMarkers
            .map { it -> it.characterIndex }
            .zipWithNext { a, b -> Pair(a, b) }
            .forEach { pair ->
                require(pair.second - pair.first > 0.0f) {
                    "Given axis markers should have ascending indices (found: ${pair.first}, ${pair.second})!"
                }
            }
}

/**
 * Returns null if the value for the requested [continuousCharacterIndex] couldn't be interpolated because it's not
 * enclosed between the markers.
 */
private fun interpolate(markers: List<AxisMarker>, continuousCharacterIndex: Float): Float? {
    markers.find { marker ->
        marker.characterIndex.toFloat() == continuousCharacterIndex
    }?.let {
        return it.value
    }

    val pairOfEnclosingMarkers = markers
            .zipWithNext()
            .find { pairOfMarkers ->
                continuousCharacterIndex in
                        pairOfMarkers.first.characterIndex.toFloat()..pairOfMarkers.second.characterIndex.toFloat()
            } ?: return null

    val (first, second) = pairOfEnclosingMarkers
    return lerpBetweenAxisMarkers(first, second, continuousCharacterIndex)
}

private fun extrapolate(markers: List<AxisMarker>, continuousCharacterIndex: Float): Float {
    val givenIndexIsBeforeMarkers = continuousCharacterIndex < markers.first().characterIndex
    val markersToExtrapolateFrom = if (givenIndexIsBeforeMarkers) {
        markers.take(2)
    } else {
        markers.takeLast(2)
    }
    return lerpBetweenAxisMarkers(markersToExtrapolateFrom[0], markersToExtrapolateFrom[1], continuousCharacterIndex)
}

/**
 * Given two axis markers and [characterIndex], the function returns a value of a linear function determined by these
 * two markers, for the argument given as [characterIndex]. It's interpolation or extrapolation, depending whether the
 * given [characterIndex] lies between the markers' indices or not.
 *
 * "Lerp" is a jargon name for linear interpolation,
 *  see https://en.wikipedia.org/wiki/Linear_interpolation#Applications.
 */
private fun lerpBetweenAxisMarkers(first: AxisMarker, second: AxisMarker, characterIndex: Float): Float {
    val normalizedDistance = (characterIndex - first.characterIndex.toFloat())/
            (second.characterIndex - first.characterIndex).toFloat()
    return first.value + (second.value - first.value)*normalizedDistance
}
