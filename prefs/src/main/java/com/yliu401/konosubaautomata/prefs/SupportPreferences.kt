package com.yliu401.konosubaautomata.prefs

import com.yliu401.konosubaautomata.prefs.core.SupportPrefsCore
import com.yliu401.konosubaautomata.prefs.core.map
import com.yliu401.konosubaautomata.scripts.prefs.ISupportPreferences

internal class SupportPreferences(
    val prefs: SupportPrefsCore
) : ISupportPreferences {
    override val friendNames by prefs.friendNames
        .map { it.toList() }

    override val preferredServants by prefs.preferredServants
        .map { it.toList() }

    override val mlb by prefs.mlb

    override val preferredCEs by prefs.preferredCEs
        .map { it.toList() }

    override val friendsOnly by prefs.friendsOnly

    override val selectionMode by prefs.selectionMode

    override val fallbackTo by prefs.fallbackTo

    override val supportClass by prefs.supportClass

    override val alsoCheckAll by prefs.alsoCheckAll

    override val maxAscended by prefs.maxAscended

    override val skill1Max by prefs.skill1Max
    override val skill2Max by prefs.skill2Max
    override val skill3Max by prefs.skill3Max
}