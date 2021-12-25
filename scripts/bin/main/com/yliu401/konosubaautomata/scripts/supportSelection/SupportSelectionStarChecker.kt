package com.yliu401.konosubaautomata.scripts.supportSelection

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.libautomata.Region
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject

@ScriptScope
class SupportSelectionStarChecker @Inject constructor(
    api: IFgoAutomataApi
): IFgoAutomataApi by api {
    fun isStarPresent(region: Region): Boolean {
        val mlbSimilarity = prefs.support.mlbSimilarity
        return region.exists(images[Images.LimitBroken], similarity = mlbSimilarity)
    }
}