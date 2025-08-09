package com.github.hirotask.ninjaoni.utils

object MathUtil {
    /**
     * Normalizes an angle to an absolute angle.
     * The normalized angle will be in the range from 0 to 360, where 360
     * itself is not included.
     *
     * @param angle the angle to normalize
     * @return the normalized angle that will be in the range of [0,360[
     */
    fun normalAbsoluteAngleDegrees(angle: Double): Double
        = if (angle % 360 >= 0) angle else angle + 360

}