package com.yliu401.libautomata.extensions

import com.yliu401.libautomata.ExitManager
import com.yliu401.libautomata.IPlatformImpl
import javax.inject.Inject
import kotlin.time.Duration

class DurationExtensions @Inject constructor(
    val platformImpl: IPlatformImpl,
    val exitManager: ExitManager
) : IDurationExtensions {
    override fun Duration.wait() {
        val multiplier = platformImpl.prefs.waitMultiplier
        exitManager.wait(this * multiplier)
    }
}