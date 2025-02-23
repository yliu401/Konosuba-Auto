package com.yliu401.konosubaautomata.scripts.models.battle

import kotlin.time.TimeSource

class RunState {
    private val timestamp = TimeSource.Monotonic.markNow()
    val runTime get() = timestamp.elapsedNow()
    var totalTurns = 0
        private set

    var stage = -1
        private set(value) {
            field = value
            stageState.stageCountSnapshot?.close()
            stageState = StageState()
            turn = -1
        }

    var stageState = StageState()
        private set

    var turn = -1
        private set(value) {
            field = value

            if (value != -1) {
                ++totalTurns
            }

            turnState = TurnState()
        }

    var turnState = TurnState()
        private set

    fun nextStage() = ++stage

    fun nextTurn() = ++turn

    var shuffled = false
}