package com.yliu401.konosubaautomata.scripts.supportSelection

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.konosubaautomata.scripts.ScriptLog
import com.yliu401.konosubaautomata.scripts.modules.Support
import com.yliu401.libautomata.Region
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject

@ScriptScope
class SupportBoundsFinder @Inject constructor(
    api: IFgoAutomataApi
): IFgoAutomataApi by api {
    fun findSupportBounds(support: Region) =
        locations.support.confirmSetupButtonRegion
            .findAll(
                images[Images.SupportConfirmSetupButton],
                Support.supportRegionToolSimilarity
            )
            .map {
                locations.support.defaultBounds
                    .copy(y = it.region.y - 70)
            }
            .firstOrNull { support in it }
            ?: locations.support.defaultBounds.also {
                messages.log(ScriptLog.DefaultSupportBounds)
            }
}