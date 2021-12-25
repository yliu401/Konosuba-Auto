package com.yliu401.konosubaautomata.scripts.models.battle

import com.yliu401.konosubaautomata.scripts.models.EnemyTarget
import com.yliu401.libautomata.IPattern

class StageState {
    var stageCountSnapshot: IPattern? = null

    var chosenTarget: EnemyTarget? = null
}