package com.yliu401.konosubaautomata.scripts.entrypoints

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.konosubaautomata.scripts.ScriptNotify
import com.yliu401.konosubaautomata.scripts.enums.GameServerEnum
import com.yliu401.konosubaautomata.scripts.enums.MaterialEnum
import com.yliu401.konosubaautomata.scripts.models.BoostItem
import com.yliu401.konosubaautomata.scripts.models.FieldSlot
import com.yliu401.konosubaautomata.scripts.models.battle.BattleState
import com.yliu401.konosubaautomata.scripts.modules.*
import com.yliu401.konosubaautomata.scripts.prefs.IPreferences
import com.yliu401.libautomata.EntryPoint
import com.yliu401.libautomata.ExitManager
import com.yliu401.libautomata.ScriptAbortException
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject
import kotlin.time.Duration

/**
 * Checks if Support Selection menu is up
 */
fun IFgoAutomataApi.isInSupport(): Boolean {
    return locations.support.screenCheckRegion.exists(images[Images.SupportScreen], similarity = 0.85)
}

fun IFgoAutomataApi.isInventoryFull() =
    // We only have images for JP and NA
    prefs.gameServer in listOf(GameServerEnum.En, GameServerEnum.Jp)
            && images[Images.InventoryFull] in locations.inventoryFullRegion

/**
 * Script for starting quests, selecting the support and doing battles.
 */
@ScriptScope
class AutoBattle @Inject constructor(
    exitManager: ExitManager,
    api: IFgoAutomataApi,
    private val state: BattleState,
    private val battle: Battle,
    private val support: Support,
    private val withdraw: Withdraw,
    private val partySelection: PartySelection,
    private val screenshotDrops: ScreenshotDrops,
    private val connectionRetry: ConnectionRetry,
    private val refill: Refill,
    private val matTracker: MaterialsTracker,
    private val ceDropsTracker: CEDropsTracker
) : EntryPoint(exitManager), IFgoAutomataApi by api {
    sealed class ExitReason {
        object Abort : ExitReason()
        class Unexpected(val e: Exception) : ExitReason()
        object CEGet : ExitReason()
        object CEDropped : ExitReason()
        object FirstClearRewards : ExitReason()
        class LimitMaterials(val count: Int) : ExitReason()
        object WithdrawDisabled : ExitReason()
        object APRanOut : ExitReason()
        object InventoryFull : ExitReason()
        class LimitRuns(val count: Int) : ExitReason()
        object SupportSelectionManual : ExitReason()
        object SupportSelectionFriendNotSet : ExitReason()
        object SupportSelectionPreferredNotSet : ExitReason()
        class SkillCommandParseError(val e: Exception) : ExitReason()
        class CardPriorityParseError(val msg: String) : ExitReason()
        object Paused : ExitReason()
        object StopAfterThisRun : ExitReason()
    }

    internal class BattleExitException(val reason: ExitReason) : Exception()

    class ExitException(val reason: ExitReason, val state: ExitState) : Exception()

    private var isContinuing = false

    override fun script(): Nothing {
        try {
            loop()
        } catch (e: BattleExitException) {
            throw ExitException(e.reason, makeExitState())
        } catch (e: ScriptAbortException) {
            throw ExitException(ExitReason.Abort, makeExitState())
        } catch (e: Exception) {
            val reason = ExitReason.Unexpected(e)

            throw ExitException(reason, makeExitState())
        } finally {
            refill.autoDecrement()
            matTracker.autoDecrement()

            val refill = prefs.refill

            // Auto-decrement runs
            if (refill.shouldLimitRuns) {
                refill.limitRuns -= state.runs

                // Turn off run limit when done
                if (refill.limitRuns <= 0) {
                    refill.limitRuns = 1
                    refill.shouldLimitRuns = false
                }
            }
        }
    }

    override fun pausedStatus() =
        ExitException(ExitReason.Paused, makeExitState())

    private fun useBoostItem() {
        val boostItem = BoostItem.of(prefs.boostItemSelectionMode)
        if (boostItem is BoostItem.Enabled) {
            locations.locate(boostItem).click()

            // in case you run out of items
            if (boostItem !is BoostItem.Enabled.Skip) {
                locations.locate(BoostItem.Enabled.Skip).click()
            }
        }
    }

    class ExitState(
        val timesRan: Int,
        val runLimit: Int?,
        val timesRefilled: Int,
        val refillLimit: Int,
        val ceDropCount: Int,
        val materials: Map<MaterialEnum, Int>,
        val matLimit: Int?,
        val withdrawCount: Int,
        val totalTime: Duration,
        val averageTimePerRun: Duration,
        val minTurnsPerRun: Int,
        val maxTurnsPerRun: Int,
        val averageTurnsPerRun: Int
    )

    private fun makeExitState(): ExitState {
        return ExitState(
            timesRan = state.runs,
            runLimit = if (prefs.refill.shouldLimitRuns) prefs.refill.limitRuns else null,
            timesRefilled = refill.timesRefilled,
            refillLimit = prefs.refill.repetitions,
            ceDropCount = ceDropsTracker.count,
            materials = matTracker.farmed,
            matLimit = if (prefs.refill.shouldLimitMats) prefs.refill.limitMats else null,
            withdrawCount = withdraw.count,
            totalTime = state.totalBattleTime,
            averageTimePerRun = state.averageTimePerRun,
            minTurnsPerRun = state.minTurnsPerRun,
            maxTurnsPerRun = state.maxTurnsPerRun,
            averageTurnsPerRun = state.averageTurnsPerRun
        )
    }

    private fun loop(): Nothing {
        // a map of validators and associated actions
        // if the validator function evaluates to true, the associated action function is called
        val screens: Map<() -> Boolean, () -> Unit> = mapOf(
            {isInNext()} to {next()},
            {isInReplay()} to {replay()},
           // {isInLeftArrow()} to {Left()},
           /* { connectionRetry.needsToRetry() } to { connectionRetry.retry() },
            { battle.isIdle() } to { battle.performBattle() },
            { isInMenu() } to { menu() },
            { isInResult() } to { result() },
            { isInDropsScreen() } to { dropScreen() },
            { isInQuestRewardScreen() } to { questReward() },
            { isInSupport() } to { support() },
            { isRepeatScreen() } to { repeatQuest() },
            { withdraw.needsToWithdraw() } to { withdraw.withdraw() },
            { needsToStorySkip() } to { skipStory() },
            { isFriendRequestScreen() } to { skipFriendRequestScreen() },
            { isBond10CEReward() } to { bond10CEReward() },
            { isCeRewardDetails() } to { ceRewardDetails() },
            { isDeathAnimation() } to { locations.battle.skipDeathAnimationClick.click() }*/
        )

        // Loop through SCREENS until a Validator returns true
        while (true) {
            val actor = useSameSnapIn {
                screens
                    .asSequence()
                    .filter { (validator, _) -> validator() }
                    .map { (_, actor) -> actor }
                    .firstOrNull()
            }

            actor?.invoke()

            Duration.seconds(1).wait()
        }
    }
    private fun isInLeftArrow() = images[Images.LeftArrow] in locations.leftArrowRegion
    private fun Left(){
        locations.leftArrowClick.click()
    }

    //If next.png is on the screen,
    private fun isInNext() = images[Images.Next] in locations.nextScreenRegion
    private fun next(){
        isContinuing = false
        battle.resetState()
        showRefillsAndRunsMessage()
        locations.nextClick.click()
    }
    //When Replay is seen on screen and clicks 2x to rerun.
    private fun isInReplay() = images[Images.Replay] in locations.replayRegion
    private fun replay(){
        locations.nextClick.click()
        Duration.seconds(0.6).wait()
        locations.clickOk.click()
    }
    //If Part 1 is on screen
    //private fun isInMainStory() = images[Images.MainstoryP1] in locations.nextScreenRegion

    /**
     *  Checks if in menu.png is on the screen, indicating that a quest can be chosen.
     */
    private fun isInMenu() = images[Images.Menu] in locations.menuScreenRegion

    /**
     * Resets the battle state, clicks on the quest and refills the AP if needed.
     */
    private fun menu() {
        // In case the repeat loop breaks and we end up in menu (like withdrawing from quests)
        isContinuing = false

        battle.resetState()

        showRefillsAndRunsMessage()

        // Click uppermost quest
        locations.menuSelectQuestClick.click()

        afterSelectingQuest()
    }

    /**
     * Checks if the Quest Completed screen is up. This can be one of many screens:
     * - Bond point distribution
     * - Bond level up
     * - Master EXP gains
     * - Dropped materials
     * - Master Level or Mystic Codes level ups
     *
     * All screens need to be included in case of getting stuck in one of them because of lags or
     * too few clicks.
     */
    private fun isInResult(): Boolean {
        val cases = sequenceOf(
            images[Images.Result] to locations.resultScreenRegion,
            images[Images.Bond] to locations.resultBondRegion,
            images[Images.MasterLevelUp] to locations.resultMasterLvlUpRegion,
            images[Images.MasterExp] to locations.resultMasterExpRegion
        )

        return cases.any { (image, region) -> image in region }
    }

    private fun isBond10CEReward() =
        locations.resultCeRewardRegion.exists(images[Images.Bond10Reward], similarity = 0.75)

    /**
     * It seems like we need to click on CE (center of screen) to accept them
     */
    private fun bond10CEReward() =
        locations.scriptArea.center.click()

    private fun isCeRewardDetails() =
        images[Images.CEDetails] in locations.resultCeRewardDetailsRegion

    private fun isDeathAnimation() =
        FieldSlot.list
            .map { locations.battle.servantPresentRegion(it) }
            .count { images[Images.ServantExist] in it } in 1..2

    private fun ceRewardDetails() {
        if (prefs.stopOnCEGet) {
            // Count the current run
            state.nextRun()

            throw BattleExitException(ExitReason.CEGet)
        } else messages.notify(ScriptNotify.CEGet)

        locations.resultCeRewardCloseClick.click()
    }

    /**
     * Clicks through the reward screens.
     */
    private fun result() =
        locations.resultClick.click(15)

    private fun isInDropsScreen() =
        images[Images.MatRewards] in locations.resultMatRewardsRegion

    private fun dropScreen() {
        ceDropsTracker.lookForCEDrops()
        matTracker.parseMaterials()
        screenshotDrops.screenshotDrops()

        locations.resultMatRewardsRegion.click()
    }

    private fun isRepeatScreen() = images[Images.Repeat] in locations.continueRegion

    private fun repeatQuest() {
        // Needed to show we don't need to enter the "StartQuest" function
        isContinuing = true

        // Pressing Continue option after completing a quest, resetting the state as would occur in "Menu" function
        battle.resetState()

        val continueButtonRegion = locations.continueRegion.find(images[Images.Repeat])?.region
            ?: return

        // If Boost items are usable, Continue button shifts to the right
        val useBoost = if (continueButtonRegion.x > locations.scriptArea.center.x + 350) {
            val boost = BoostItem.of(prefs.boostItemSelectionMode)

            boost is BoostItem.Enabled && boost != BoostItem.Enabled.Skip
        } else false

        if (useBoost) {
            locations.continueBoostClick.click()
            useBoostItem()
        } else continueButtonRegion.click()

        showRefillsAndRunsMessage()

        // If Stamina is empty, follow same protocol as is in "Menu" function Auto refill.
        afterSelectingQuest()
    }

    private fun isFriendRequestScreen() =
        images[Images.SupportExtra] in locations.resultFriendRequestRegion

    private fun skipFriendRequestScreen() {
        // Friend request dialogue. Appears when non-friend support was selected this battle. Ofc it's defaulted not sending request.
        locations.resultFriendRequestRejectClick.click()
    }

    /**
     * Checks if FGO is on the quest reward screen for Mana Prisms, SQ, ...
     */
    private fun isInQuestRewardScreen() =
        images[Images.QuestReward] in locations.resultQuestRewardRegion

    /**
     * Handles the quest rewards screen.
     */
    private fun questReward() {
        if (prefs.stopOnFirstClearRewards) {
            // Count the current run
            state.nextRun()

            throw BattleExitException(ExitReason.FirstClearRewards)
        }

        locations.resultClick.click()
    }

    // Selections Support option
    private fun support() {
        support.selectSupport()

        if (!isContinuing) {
            Duration.seconds(4).wait()
            startQuest()

            // Wait timer till battle starts.
            // Uses less battery to wait than to search for images for a few seconds.
            // Adjust according to device.
            Duration.seconds(5).wait()
        }
    }

    /**
     * Checks if the SKIP button exists on the screen.
     */
    private fun needsToStorySkip() =
        prefs.storySkip && locations.menuStorySkipRegion.exists(images[Images.StorySkip], similarity = 0.7)

    private fun skipStory() {
        locations.menuStorySkipClick.click()
        Duration.seconds(0.5).wait()
        locations.menuStorySkipYesClick.click()
    }

    /**
     * Starts the quest after the support has already been selected. The following features are done optionally:
     * 1. The configured party is selected if it is set in the selected AutoSkill config
     * 2. A boost item is selected if [IPreferences.boostItemSelectionMode] is set (needed in some events)
     * 3. The story is skipped if [IPreferences.storySkip] is activated
     */
    private fun startQuest() {
        partySelection.selectParty()

        locations.menuStartQuestClick.click()

        Duration.seconds(2).wait()

        useBoostItem()
    }

    /**
     * Will show a toast informing the user of number of runs and how many apples have been used so far.
     */
    private fun showRefillsAndRunsMessage() =
        messages.notify(
            ScriptNotify.BetweenRuns(
                refills = refill.timesRefilled,
                runs = state.runs
            )
        )

    private fun afterSelectingQuest() {
        Duration.seconds(1.5).wait()

        if (isInventoryFull()) {
            throw BattleExitException(ExitReason.InventoryFull)
        }

        refill.refill()
    }
}
