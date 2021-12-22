package com.yliu401.konosubaautomata.scripts.prefs

import com.yliu401.konosubaautomata.scripts.enums.GameServerEnum
import com.yliu401.konosubaautomata.scripts.enums.ScriptModeEnum
import com.yliu401.libautomata.IPlatformPrefs
import kotlin.time.Duration

interface IPreferences {
    var scriptMode: ScriptModeEnum
    var gameServer: GameServerEnum
    val skillConfirmation: Boolean
    val battleConfigs: List<IBattleConfig>
    var selectedBattleConfig: IBattleConfig
    val storySkip: Boolean
    val withdrawEnabled: Boolean
    val stopOnCEDrop: Boolean
    val stopOnCEGet: Boolean
    val stopOnFirstClearRewards: Boolean
    val boostItemSelectionMode: Int
    val refill: IRefillPreferences
    var waitAPRegen: Boolean
    val ignoreNotchCalculation: Boolean
    val useRootForScreenshots: Boolean
    val recordScreen: Boolean
    val skillDelay: Duration
    val screenshotDrops: Boolean
    var maxGoldEmberSetSize: Int
    var stopAfterThisRun: Boolean
    val skipServantFaceCardCheck: Boolean

    var shouldLimitFP: Boolean
    var limitFP: Int
    var preventLotteryBoxReset: Boolean
    var receiveEmbersWhenGiftBoxFull: Boolean

    val stageCounterSimilarity: Double
    val stageCounterNew: Boolean
    val waitBeforeTurn: Duration
    val waitBeforeCards: Duration

    val support: ISupportPreferencesCommon
    val platformPrefs: IPlatformPrefs
    val gestures: IGesturesPreferences

    var ceBombTargetRarity: Int

    fun forBattleConfig(id: String): IBattleConfig
    fun addBattleConfig(id: String): IBattleConfig
    fun removeBattleConfig(id: String)
}

val IPreferences.wantsMediaProjectionToken get() = !useRootForScreenshots

val IPreferences.isNewUI get() = gameServer in listOf(GameServerEnum.Jp, GameServerEnum.En, GameServerEnum.Kr, GameServerEnum.Cn)