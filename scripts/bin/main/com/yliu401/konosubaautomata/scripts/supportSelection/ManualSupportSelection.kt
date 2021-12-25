package com.yliu401.konosubaautomata.scripts.supportSelection

import com.yliu401.konosubaautomata.scripts.entrypoints.AutoBattle

object ManualSupportSelection: SupportSelectionProvider {
    override fun select() = throw AutoBattle.BattleExitException(AutoBattle.ExitReason.SupportSelectionManual)
}