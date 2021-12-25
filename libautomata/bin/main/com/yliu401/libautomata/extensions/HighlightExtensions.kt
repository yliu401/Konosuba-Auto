package com.yliu401.libautomata.extensions

import com.yliu401.libautomata.ExitManager
import com.yliu401.libautomata.HighlightColor
import com.yliu401.libautomata.IPlatformImpl
import com.yliu401.libautomata.Region
import javax.inject.Inject
import kotlin.time.Duration

class HighlightExtensions @Inject constructor(
    val exitManager: ExitManager,
    val platformImpl: IPlatformImpl,
    transformationExtensions: ITransformationExtensions
) : IHighlightExtensions, ITransformationExtensions by transformationExtensions {
    override fun Region.highlight(color: HighlightColor, duration: Duration) {
        exitManager.checkExitRequested()
        if (platformImpl.prefs.debugMode) {
            platformImpl.highlight(this.transform(), duration, color)
        }
    }
}