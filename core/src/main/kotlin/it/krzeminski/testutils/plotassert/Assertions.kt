package it.krzeminski.testutils.plotassert

fun assertFunctionConformsTo(
    functionUnderTest: (Float) -> Float,
    visualisation: PlotConstraintsBuilder.() -> Unit
) {
    val rawVisualisation = readRawVisualisation(visualisation)
    val constraints = rawVisualisation.toConstraints()
    constraints.map { constraint ->
        constraint.assertMatches(functionUnderTest)
    }
}
