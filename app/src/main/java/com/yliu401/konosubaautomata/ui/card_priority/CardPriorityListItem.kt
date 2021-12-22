package com.yliu401.konosubaautomata.ui.card_priority

import androidx.compose.runtime.MutableState
import com.yliu401.konosubaautomata.scripts.enums.BraveChainEnum
import com.yliu401.konosubaautomata.scripts.models.CardScore
import com.yliu401.konosubaautomata.scripts.models.TeamSlot

data class CardPriorityListItem(
    val scores: MutableList<CardScore>,
    val servantPriority: MutableList<TeamSlot>,
    var rearrangeCards: MutableState<Boolean>,
    var braveChains: MutableState<BraveChainEnum>
)