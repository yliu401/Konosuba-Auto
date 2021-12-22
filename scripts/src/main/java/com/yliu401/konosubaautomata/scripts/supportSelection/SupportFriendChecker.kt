package com.yliu401.konosubaautomata.scripts.supportSelection

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.konosubaautomata.scripts.enums.SupportSelectionModeEnum
import com.yliu401.konosubaautomata.scripts.prefs.ISupportPreferences
import com.yliu401.libautomata.Region
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject

@ScriptScope
class SupportFriendChecker @Inject constructor(
    private val supportPrefs: ISupportPreferences,
    api: IFgoAutomataApi
): IFgoAutomataApi by api {
    fun isFriend(region: Region = locations.support.friendRegion): Boolean {
        val onlySelectFriends = supportPrefs.friendsOnly
                || supportPrefs.selectionMode == SupportSelectionModeEnum.Friend

        if (!onlySelectFriends)
            return true

        return sequenceOf(
            images[Images.Friend],
            images[Images.Guest],
            images[Images.Follow]
        ).any { it in region }
    }
}