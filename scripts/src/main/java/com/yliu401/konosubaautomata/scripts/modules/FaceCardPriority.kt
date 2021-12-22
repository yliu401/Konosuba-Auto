package com.yliu401.konosubaautomata.scripts.modules

import com.yliu401.konosubaautomata.scripts.enums.CardTypeEnum
import com.yliu401.konosubaautomata.scripts.models.CardPriorityPerWave
import com.yliu401.konosubaautomata.scripts.models.CardScore
import com.yliu401.konosubaautomata.scripts.models.ParsedCard
import com.yliu401.konosubaautomata.scripts.models.ServantPriorityPerWave
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject

@ScriptScope
class FaceCardPriority @Inject constructor(
    private val cardPriority: CardPriorityPerWave,
    private val servantPriority: ServantPriorityPerWave?
) {

    private fun applyCardPriority(
        cards: List<ParsedCard>,
        stage: Int
    ): List<ParsedCard> {
        val groupedByScore = cards.groupBy { CardScore(it.type, it.affinity) }

        return cardPriority
            .atWave(stage)
            .mapNotNull { groupedByScore[it] }
            .flatten()
    }

    private fun applyServantPriority(
        cards: List<ParsedCard>,
        priority: ServantPriorityPerWave,
        stage: Int
    ): List<ParsedCard> {
        val groupedByServant = cards.groupBy { it.servant }

        return priority
            .atWave(stage)
            .mapNotNull { groupedByServant[it] }
            .map { servantCards ->
                applyCardPriority(
                    // Stunned cards at the end
                    cards = servantCards.filter { it.type != CardTypeEnum.Unknown },
                    stage = stage
                )
            }
            .flatten()
            .let { picked ->
                // In case less than 3 cards are picked
                val notPicked = cards - picked

                picked + notPicked
            }
    }

    fun sort(
        cards: List<ParsedCard>,
        stage: Int
    ): List<ParsedCard> =
        servantPriority
            ?.let { applyServantPriority(cards, it, stage) }
            ?: applyCardPriority(cards, stage)
}