package com.yliu401.konosubaautomata.ui.prefs

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yliu401.konosubaautomata.prefs.core.Pref
import com.yliu401.konosubaautomata.ui.FgaDialog
import com.yliu401.konosubaautomata.ui.VectorIcon
import com.yliu401.konosubaautomata.ui.singleChoiceList

@Composable
fun <T> listDialog(
    selected: T,
    onSelectedChange: (T) -> Unit,
    entries: Map<T, String>,
    title: String
): FgaDialog {
    val dialog = FgaDialog()

    dialog.build {
        title(text = title)

        singleChoiceList(
            selected = selected,
            onSelectedChange = {
                onSelectedChange(it)
                hide()
            },
            items = entries.keys.toList()
        ) {
            Text(entries[it] ?: "--")
        }
    }

    return dialog
}

@Composable
fun <T> Pref<T>.ListPreference(
    title: String,
    entries: Map<T, String>,
    modifier: Modifier = Modifier,
    summary: String = "",
    singleLineTitle: Boolean = false,
    icon: VectorIcon? = null,
    enabled: Boolean = true
) {
    var selected by remember()

    val dialog = listDialog(
        selected = selected,
        onSelectedChange = { selected = it },
        entries = entries,
        title = title
    )

    Preference(
        title = title,
        summary = entries[selected] ?: summary,
        singleLineTitle = singleLineTitle,
        icon = icon,
        enabled = enabled,
        onClick = { dialog.show() },
        modifier = modifier
    )
}