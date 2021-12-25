package com.yliu401.libautomata.extensions

import com.yliu401.libautomata.HighlightColor
import com.yliu401.libautomata.Region
import kotlin.time.Duration

interface IHighlightExtensions {
    /**
     * Adds borders around the [Region].
     *
     * @param duration how long the borders should be displayed
     */
    fun Region.highlight(color: HighlightColor, duration: Duration = Duration.seconds(0.3))
}