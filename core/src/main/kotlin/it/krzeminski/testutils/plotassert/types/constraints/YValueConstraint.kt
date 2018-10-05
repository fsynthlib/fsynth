package it.krzeminski.testutils.plotassert.types.constraints

abstract class YValueConstraint {
    abstract fun assertMatches(yValue: Float)
}
