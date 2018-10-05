package it.krzeminski.utils

import kotlin.math.abs

infix fun Float.almostEqualTo(other: Float) =
        abs(this - other) < 0.000001f
