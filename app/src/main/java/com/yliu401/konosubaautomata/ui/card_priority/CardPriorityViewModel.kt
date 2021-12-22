package com.yliu401.konosubaautomata.ui.card_priority

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.yliu401.konosubaautomata.prefs.core.BattleConfigCore
import com.yliu401.konosubaautomata.prefs.core.PrefsCore
import com.yliu401.konosubaautomata.scripts.enums.BraveChainEnum
import com.yliu401.konosubaautomata.scripts.models.CardPriority
import com.yliu401.konosubaautomata.scripts.models.CardPriorityPerWave
import com.yliu401.konosubaautomata.scripts.models.ServantPriorityPerWave
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardPriorityViewModel @Inject constructor(
    val prefsCore: PrefsCore,
    val battleConfig: BattleConfigCore
) : ViewModel() {
    val cardPriorityItems: SnapshotStateList<CardPriorityListItem> by lazy {
        val cardPriority = battleConfig.cardPriority.get()
        val servantPriority = battleConfig.servantPriority.get()

        val rearrangeCards = battleConfig.rearrangeCards.get()
        val braveChains = battleConfig.braveChains.get()

        cardPriority
            .take(3)
            .map { it.toMutableList() }
            .withIndex()
            .map {
                CardPriorityListItem(
                    it.value,
                    servantPriority.atWave(it.index).toMutableList(),
                    mutableStateOf(rearrangeCards.getOrElse(it.index) { false }),
                    mutableStateOf(braveChains.getOrElse(it.index) { BraveChainEnum.None })
                )
            }
            .toMutableStateList()
    }

    val useServantPriority = battleConfig.useServantPriority

    fun save() {
        battleConfig.cardPriority.set(
            CardPriorityPerWave.from(
                cardPriorityItems.map { CardPriority.from(it.scores) }
            )
        )

        battleConfig.servantPriority.set(
            ServantPriorityPerWave.from(
                cardPriorityItems.map { it.servantPriority }
            )
        )

        battleConfig.rearrangeCards.set(cardPriorityItems.map { it.rearrangeCards.value })
        battleConfig.braveChains.set(cardPriorityItems.map { it.braveChains.value })
    }
}