package com.yliu401.konosubaautomata.ui.fine_tune

import androidx.annotation.StringRes
import com.yliu401.konosubaautomata.prefs.core.Pref
import com.yliu401.konosubaautomata.ui.VectorIcon

class FineTuneItem(
    val pref: Pref<Int>,
    @StringRes val name: Int,
    val icon: VectorIcon,
    val valueRange: IntRange = 0..100,
    val valueRepresentation: (Int) -> String = { it.toString() },
    // TODO: Localize fine-tune hints
    val hint: String = ""
) {
    fun reset() {
        pref.resetToDefault()
    }
}

class FineTuneGroup(
    @StringRes val name: Int,
    val items: List<FineTuneItem>
)