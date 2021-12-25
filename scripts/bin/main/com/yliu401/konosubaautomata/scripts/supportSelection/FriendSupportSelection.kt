package com.yliu401.konosubaautomata.scripts.supportSelection

import com.yliu401.konosubaautomata.SupportImageKind
import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.entrypoints.AutoBattle
import com.yliu401.konosubaautomata.scripts.prefs.ISupportPreferences
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject

@ScriptScope
class FriendSupportSelection @Inject constructor(
    supportPrefs: ISupportPreferences,
    boundsFinder: SupportBoundsFinder,
    friendChecker: SupportFriendChecker,
    api: IFgoAutomataApi
): SpecificSupportSelection(supportPrefs, boundsFinder, friendChecker, api) {
    private val friendNames = supportPrefs.friendNames

    override fun search(): SpecificSupportSearchResult {
        if (friendNames.isEmpty()) {
            throw AutoBattle.BattleExitException(AutoBattle.ExitReason.SupportSelectionFriendNotSet)
        }

        for (friendName in friendNames) {
            // Cached pattern. Don't dispose here.
            val patterns = images.loadSupportPattern(SupportImageKind.Friend, friendName)

            patterns.forEach { pattern ->
                for (friend in locations.support.friendsRegion.findAll(pattern).sorted()) {
                    return SpecificSupportSearchResult.Found(friend.region)
                }
            }
        }

        return SpecificSupportSearchResult.NotFound
    }
}

