package com.yliu401.konosubaautomata.scripts

import com.yliu401.konosubaautomata.scripts.models.CommandCard
import com.yliu401.konosubaautomata.scripts.models.FieldSlot
import com.yliu401.konosubaautomata.scripts.models.TeamSlot
import kotlin.time.Duration

sealed class ScriptNotify {
    object CEGet: ScriptNotify()
    object CEDropped: ScriptNotify()
    class WaitForAPRegen(val minutes: Int = 1): ScriptNotify()
    class FailedToDetermineCards(val cards: List<CommandCard.Face>): ScriptNotify()
    class SupportListUpdatingIn(val time: Duration): ScriptNotify()
    class BetweenRuns(val refills: Int, val runs: Int): ScriptNotify()
}

sealed class ScriptLog {
    object DefaultSupportBounds: ScriptLog()
    object DefaultMasterOffset: ScriptLog()
    class CurrentParty(val party: Int?): ScriptLog()
    class MaxSkills(
        val needMaxedSkills: List<Boolean>,
        val isSkillMaxed: List<Boolean>
    ): ScriptLog()
    class ClickingNPs(val nps: Iterable<CommandCard.NP>): ScriptLog()
    class ClickingCards(val cards: Iterable<CommandCard.Face>): ScriptLog()
    class NPsGroupedByFaceCards(val groups: Map<CommandCard.NP, List<CommandCard.Face>>): ScriptLog()
    class SupportFaceCardGroup(val group: List<CommandCard.Face>): ScriptLog()
    class FaceCardGroups(val groups: List<List<CommandCard.Face>>): ScriptLog()
    class ServantEnteredSlot(
        val servant: TeamSlot,
        val slot: FieldSlot
    ): ScriptLog()
    class CardsBelongToServant(
        val cards: List<CommandCard.Face>,
        val servant: TeamSlot,
        val isSupport: Boolean = false
    ): ScriptLog()
}

interface IScriptMessages {
    fun notify(action: ScriptNotify)

    fun log(item: ScriptLog)
}