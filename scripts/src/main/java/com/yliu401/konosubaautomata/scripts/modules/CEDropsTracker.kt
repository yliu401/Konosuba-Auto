package com.yliu401.konosubaautomata.scripts.modules

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.konosubaautomata.scripts.ScriptNotify
import com.yliu401.konosubaautomata.scripts.entrypoints.AutoBattle
import com.yliu401.konosubaautomata.scripts.models.battle.BattleState
import com.yliu401.libautomata.Region
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject

@ScriptScope
class CEDropsTracker @Inject constructor(
    api: IFgoAutomataApi,
    private val state: BattleState
) : IFgoAutomataApi by api {
    var count = 0
        private set

    fun lookForCEDrops() {
        val starsRegion = Region(40, -40, 80, 40)

        val ceDropped = locations.scriptArea
            .findAll(images[Images.DropCE])
            .map { (region, _) ->
                starsRegion + region.location
            }
            .count { images[Images.DropCEStars] in it }

        if (ceDropped > 0) {
            count += ceDropped

            if (prefs.stopOnCEDrop) {
                // Count the current run
                state.nextRun()

                throw AutoBattle.BattleExitException(AutoBattle.ExitReason.CEDropped)
            } else messages.notify(ScriptNotify.CEDropped)
        }
    }
}