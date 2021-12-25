package com.yliu401.konosubaautomata.scripts.entrypoints

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.libautomata.EntryPoint
import com.yliu401.libautomata.ExitManager
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject
import kotlin.time.Duration

/**
 * Continually triggers 10x Summon, intended for FP summons, but could also be used for SQ summons.
 */
@ScriptScope
class AutoFriendGacha @Inject constructor(
    exitManager: ExitManager,
    api: IFgoAutomataApi
) : EntryPoint(exitManager), IFgoAutomataApi by api {
    sealed class ExitReason {
        object InventoryFull: ExitReason()
        class Limit(val count: Int): ExitReason()
    }

    class ExitException(val reason: ExitReason): Exception()

    private var count = 0

    private fun countNext() {
        if (prefs.shouldLimitFP && count >= prefs.limitFP) {
            throw ExitException(ExitReason.Limit(count))
        }
        ++count
    }

    override fun script(): Nothing {
        if (images[Images.FPSummonContinue] !in locations.fp.continueSummonRegion) {
            locations.fp.first10SummonClick.click()
            Duration.seconds(0.3).wait()
            locations.fp.okClick.click()

            countNext()
        }

        while (true) {
            if (isInventoryFull()) {
                throw ExitException(ExitReason.InventoryFull)
            }

            if (images[Images.FPSummonContinue] in locations.fp.continueSummonRegion) {
                countNext()

                locations.fp.continueSummonClick.click()
                Duration.seconds(0.3).wait()
                locations.fp.okClick.click()
                Duration.seconds(3).wait()
            } else locations.fp.skipRapidClick.click(15)
        }
    }
}