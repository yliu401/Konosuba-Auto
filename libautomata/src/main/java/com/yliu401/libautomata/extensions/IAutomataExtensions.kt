package com.yliu401.libautomata.extensions

import com.yliu401.libautomata.IPattern
import com.yliu401.libautomata.Region

interface IAutomataExtensions : IDurationExtensions,
    IGestureExtensions,
    IHighlightExtensions,
    IImageMatchingExtensions,
    ITransformationExtensions {
    /**
     * Gets the image content of this Region.
     *
     * @return an [IPattern] object with the image data
     */
    fun Region.getPattern(): IPattern

    fun <T> useSameSnapIn(block: () -> T): T

    fun <T> useColor(block: () -> T): T
}