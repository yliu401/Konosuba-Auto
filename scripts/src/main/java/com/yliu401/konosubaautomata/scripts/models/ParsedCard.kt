package com.yliu401.konosubaautomata.scripts.models

import com.yliu401.konosubaautomata.scripts.enums.CardAffinityEnum
import com.yliu401.konosubaautomata.scripts.enums.CardTypeEnum

data class ParsedCard(
    val card: CommandCard.Face,
    val servant: TeamSlot,
    val fieldSlot: FieldSlot?,
    val type: CardTypeEnum,
    val affinity: CardAffinityEnum = CardAffinityEnum.Normal,
    val isStunned: Boolean = false
) {
    override fun equals(other: Any?) =
        other is ParsedCard && card == other.card

    override fun hashCode() = card.hashCode()
}