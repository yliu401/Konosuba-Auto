package com.yliu401.konosubaautomata.scripts.supportSelection

sealed class SupportSelectionResult {
    object Refresh: SupportSelectionResult()
    object ScrollDown: SupportSelectionResult()
    object Done: SupportSelectionResult()
}