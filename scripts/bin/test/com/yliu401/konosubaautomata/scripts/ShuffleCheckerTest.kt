package com.yliu401.konosubaautomata.scripts

import com.yliu401.konosubaautomata.scripts.enums.ShuffleCardsEnum
import com.yliu401.konosubaautomata.scripts.models.NPUsage
import com.yliu401.konosubaautomata.scripts.modules.ShuffleChecker
import org.junit.Assert
import org.junit.Test

class ShuffleCheckerTest {
    @Test
    fun noShuffle() {
        val checker = ShuffleChecker()

        val should = checker.shouldShuffle(ShuffleCardsEnum.None, FaceCardPriorityTest.lineup1, NPUsage.none)
        Assert.assertFalse(should)
    }
}