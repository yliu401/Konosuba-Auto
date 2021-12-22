package com.yliu401.konosubaautomata.util

import com.yliu401.libautomata.EntryPoint

sealed class ScriptState {
    object Stopped : ScriptState()
    class Started(
        val entryPoint: EntryPoint,
        val recording: AutoCloseable?,
        var paused: Boolean = false
    ) : ScriptState()

    class Stopping(val start: Started) : ScriptState()
}