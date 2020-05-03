package it.krzeminski.fsynth.postprocessing

import kotlin.math.roundToInt

/**
 * Given some sample value as [Float] (that is, 32-bit precision), converts the value so that the whole <-1.0; 1.0>
 * range is divided into [targetLevels] buckets. For example, if [targetLevels] is 2, all values below 0 will become -1,
 * and above 0 will become 1.
 *
 * This function is useful to simulate what would happen if there was less bits per sample e.g. in some data
 * transmission, when considering sound. It allows checking what is the quality degradation given the amount of data
 * designated for the number of levels per sample.
 */
fun Float.reduceLevelsPerSample(targetLevels: Int): Float {
    val maxValue = targetLevels - 1
    val normalizedInput = (this + 1.0f) / 2.0f
    val valueScaledToMax = normalizedInput * maxValue
    val rounded = valueScaledToMax.roundToInt().toFloat()
    return rounded * 2.0f / maxValue - 1.0f
}
