package com.yliu401.konosubaautomata.scripts.prefs

import com.yliu401.konosubaautomata.scripts.enums.RefillResourceEnum

interface IRefillPreferences {
    var repetitions: Int
    val resources: List<RefillResourceEnum>
    fun updateResources(resources: Set<RefillResourceEnum>)

    var shouldLimitRuns: Boolean
    var limitRuns: Int

    var shouldLimitMats: Boolean
    var limitMats: Int
}