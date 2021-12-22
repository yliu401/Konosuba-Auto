package com.yliu401.konosubaautomata.ui.prefs

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yliu401.konosubaautomata.prefs.core.Pref
import com.yliu401.konosubaautomata.ui.Stepper
import com.yliu401.konosubaautomata.ui.VectorIcon

@Composable
fun Pref<Int>.StepperPreference(
    title: String,
    modifier: Modifier = Modifier,
    icon: VectorIcon? = null,
    valueRange: IntRange = 0..100,
    enabled: Boolean = true,
    valueRepresentation: (Int) -> String = { it.toString() }
) {
    var state by remember()

    Preference(
        title = { Text(title) },
        summary = {
            Stepper(
                value = state,
                onValueChange = { state = it },
                valueRange = valueRange,
                valueRepresentation = valueRepresentation
            )
        },
        icon = icon,
        enabled = enabled,
        modifier = modifier
    )
}