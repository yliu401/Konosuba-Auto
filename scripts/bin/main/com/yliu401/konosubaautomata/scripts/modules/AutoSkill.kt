package com.yliu401.konosubaautomata.scripts.modules

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.models.AutoSkillAction
import com.yliu401.konosubaautomata.scripts.models.AutoSkillCommand
import com.yliu401.konosubaautomata.scripts.models.NPUsage
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject

@ScriptScope
class AutoSkill @Inject constructor(
    api: IFgoAutomataApi,
    private val skillCommand: AutoSkillCommand,
    private val caster: Caster
) : IFgoAutomataApi by api {
    private fun act(action: AutoSkillAction) = when (action) {
        is AutoSkillAction.Atk -> { }
        is AutoSkillAction.ServantSkill -> caster.castServantSkill(action.skill, action.target)
        is AutoSkillAction.MasterSkill -> caster.castMasterSkill(action.skill, action.target)
        is AutoSkillAction.TargetEnemy -> caster.selectEnemyTarget(action.enemy)
        is AutoSkillAction.OrderChange -> caster.orderChange(action)
    }

    fun execute(stage: Int, turn: Int): NPUsage {
        val commandList = skillCommand[stage, turn]
        var npUsage = NPUsage.none

        if (commandList.isNotEmpty()) {
            for (action in commandList) {
                if (action is AutoSkillAction.Atk) {
                    npUsage = action.toNPUsage()
                }

                act(action)
            }
        }

        return npUsage
    }
}