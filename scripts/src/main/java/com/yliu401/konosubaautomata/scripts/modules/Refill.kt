package com.yliu401.konosubaautomata.scripts.modules

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.konosubaautomata.scripts.ScriptNotify
import com.yliu401.konosubaautomata.scripts.entrypoints.AutoBattle
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject
import kotlin.time.Duration

@ScriptScope
class Refill @Inject constructor(
    api: IFgoAutomataApi
) : IFgoAutomataApi by api {
    var timesRefilled = 0
        private set

    /**
     * Refills the AP with apples depending on preferences.
     * If needed, loops and wait for AP regeneration
     */
    private fun refillOnce() {
        val refill = prefs.refill

        if (refill.resources.isNotEmpty()
            && timesRefilled < refill.repetitions
        ) {
            refill.resources
                .map { locations.locate(it) }
                .forEach { it.click() }

            Duration.seconds(1).wait()
            locations.staminaOkClick.click()
            ++timesRefilled

            Duration.seconds(3).wait()
        } else if (prefs.waitAPRegen) {
            locations.staminaCloseClick.click()

            messages.notify(ScriptNotify.WaitForAPRegen())

            Duration.seconds(60).wait()
        } else throw AutoBattle.BattleExitException(AutoBattle.ExitReason.APRanOut)
    }

    fun refill() {
        while (images[Images.Stamina] in locations.staminaScreenRegion) {
            refillOnce()
        }
    }

    fun autoDecrement() {
        val refill = prefs.refill

        // Auto-decrement apples
        refill.repetitions -= timesRefilled
    }
}