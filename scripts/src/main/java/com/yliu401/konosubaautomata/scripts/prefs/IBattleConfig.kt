package com.yliu401.konosubaautomata.scripts.prefs

import com.yliu401.konosubaautomata.scripts.enums.BraveChainEnum
import com.yliu401.konosubaautomata.scripts.enums.GameServerEnum
import com.yliu401.konosubaautomata.scripts.enums.MaterialEnum
import com.yliu401.konosubaautomata.scripts.enums.ShuffleCardsEnum
import com.yliu401.konosubaautomata.scripts.models.CardPriorityPerWave
import com.yliu401.konosubaautomata.scripts.models.ServantPriorityPerWave
import com.yliu401.konosubaautomata.scripts.models.ServantSpamConfig

interface IBattleConfig {
    val id: String
    var name: String
    var skillCommand: String
    var cardPriority: CardPriorityPerWave
    val useServantPriority: Boolean
    val servantPriority: ServantPriorityPerWave
    val rearrangeCards: List<Boolean>
    val braveChains: List<BraveChainEnum>
    val party: Int
    val materials: Set<MaterialEnum>
    val support: ISupportPreferences
    val shuffleCards: ShuffleCardsEnum
    val shuffleCardsWave: Int

    var spam: List<ServantSpamConfig>
    val autoChooseTarget: Boolean

    val server: GameServerEnum?

    fun export(): Map<String, *>

    fun import(map: Map<String, *>)
}