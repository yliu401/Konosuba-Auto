package com.yliu401.konosubaautomata.ui.runner

sealed class ScriptRunnerUIAction {
    object Start: ScriptRunnerUIAction()
    object Pause: ScriptRunnerUIAction()
    object Resume: ScriptRunnerUIAction()
    object Stop: ScriptRunnerUIAction()
    class Status(val status: Exception): ScriptRunnerUIAction()
}