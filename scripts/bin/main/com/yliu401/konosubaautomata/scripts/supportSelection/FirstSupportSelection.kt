package com.yliu401.konosubaautomata.scripts.supportSelection

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject
import kotlin.time.Duration

@ScriptScope
class FirstSupportSelection @Inject constructor(
    api: IFgoAutomataApi
): SupportSelectionProvider, IFgoAutomataApi by api {
    override fun select(): SupportSelectionResult {
        Duration.seconds(0.5).wait()

        locations.support.firstSupportClick.click()

        // Handle the case of a friend not having set a support servant
        val supportPicked = locations.support.screenCheckRegion.waitVanish(
            images[Images.SupportScreen],
            similarity = 0.85,
            timeout = Duration.seconds(10)
        )

        return if (supportPicked)
            SupportSelectionResult.Done
        else SupportSelectionResult.Refresh
    }
}