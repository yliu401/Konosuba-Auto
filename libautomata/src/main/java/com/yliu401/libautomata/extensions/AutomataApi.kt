package com.yliu401.libautomata.extensions

import com.yliu401.libautomata.ColorManager
import com.yliu401.libautomata.HighlightColor
import com.yliu401.libautomata.Region
import com.yliu401.libautomata.ScreenshotManager
import javax.inject.Inject

class AutomataApi @Inject constructor(
    private val screenshotManager: ScreenshotManager,
    durationExtensions: IDurationExtensions,
    gestureExtensions: IGestureExtensions,
    highlightExtensions: IHighlightExtensions,
    imageMatchingExtensions: IImageMatchingExtensions,
    transformationExtensions: ITransformationExtensions,
    private val colorManager: ColorManager
) : IAutomataExtensions,
    IDurationExtensions by durationExtensions,
    IGestureExtensions by gestureExtensions,
    IHighlightExtensions by highlightExtensions,
    IImageMatchingExtensions by imageMatchingExtensions,
    ITransformationExtensions by transformationExtensions {

    override fun Region.getPattern() =
        screenshotManager.getScreenshot()
            .crop(this.transformToImage())
            .also { highlight(HighlightColor.Info) }
            .copy() // It is important that the image gets cloned here.

    override fun <T> useSameSnapIn(block: () -> T) =
        screenshotManager.useSameSnapIn(block)

    override fun <T> useColor(block: () -> T): T =
        colorManager.useColor(block)
}

