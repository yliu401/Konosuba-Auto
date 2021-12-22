package com.yliu401.konosubaautomata.prefs

import com.yliu401.konosubaautomata.prefs.core.RefillPrefsCore
import com.yliu401.konosubaautomata.prefs.core.map
import com.yliu401.konosubaautomata.scripts.enums.RefillResourceEnum
import com.yliu401.konosubaautomata.scripts.prefs.IRefillPreferences

internal class RefillPreferences(
    val prefs: RefillPrefsCore
): IRefillPreferences {
    override var repetitions by prefs.repetitions

    override val resources by prefs.resources.map { set ->
        set.sortedBy { it.ordinal }
    }

    override fun updateResources(resources: Set<RefillResourceEnum>) =
        prefs.resources.set(resources)

    override var shouldLimitRuns by prefs.shouldLimitRuns
    override var limitRuns by prefs.limitRuns

    override var shouldLimitMats by prefs.shouldLimitMats
    override var limitMats by prefs.limitMats
}