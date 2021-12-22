package com.yliu401.konosubaautomata.ui.prefs

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yliu401.konosubaautomata.prefs.core.Pref
import com.yliu401.konosubaautomata.ui.VectorIcon

@Composable
fun Pref<Boolean>.SwitchPreference(
    title: String,
    modifier: Modifier = Modifier,
    summary: String = "",
    singleLineTitle: Boolean = false,
    icon: VectorIcon? = null,
    enabled: Boolean = true
) {
    var state by remember()

    Preference(
        title = title,
        summary = summary,
        singleLineTitle = singleLineTitle,
        icon = icon,
        enabled = enabled,
        onClick = { state = !state },
        modifier = modifier
    ) {
        Switch(
            checked = state,
            onCheckedChange = { state = it },
            enabled = enabled
        )
    }
}