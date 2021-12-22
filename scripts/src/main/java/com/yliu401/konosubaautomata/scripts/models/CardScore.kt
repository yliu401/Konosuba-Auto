package com.yliu401.konosubaautomata.scripts.models

import com.yliu401.konosubaautomata.scripts.enums.CardAffinityEnum
import com.yliu401.konosubaautomata.scripts.enums.CardTypeEnum

data class CardScore(val type: CardTypeEnum, val affinity: CardAffinityEnum) {
    private fun String.filterCapitals(): String {
        return this
            .asSequence()
            .filter { it.isUpperCase() }
            .joinToString(separator = "")
    }

    override fun toString(): String {
        var result = ""

        if (affinity != CardAffinityEnum.Normal) {
            result += "$affinity "
        }

        result += type

        return result.filterCapitals()
    }
}