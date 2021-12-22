package com.yliu401.konosubaautomata.ui.pref_support

import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.yliu401.konosubaautomata.R
import com.yliu401.konosubaautomata.prefs.core.Pref
import com.yliu401.konosubaautomata.ui.DimmedIcon
import com.yliu401.konosubaautomata.ui.VectorIcon
import com.yliu401.konosubaautomata.ui.icon
import com.yliu401.konosubaautomata.ui.prefs.MultiSelectListPreference
import com.yliu401.konosubaautomata.ui.prefs.remember

@Composable
fun Pref<Set<String>>.SupportSelectPreference(
    title: String,
    entries: Map<String, String>,
    icon: VectorIcon? = null
) {
    val value by remember()

    MultiSelectListPreference(
        title = title,
        entries = entries,
        icon = icon,
        summary = {
            if (it.isEmpty())
                stringResource(R.string.p_not_set)
            else it.joinToString()
        }
    ) {
        if (value.isNotEmpty()) {
            IconButton(
                onClick = { resetToDefault() }
            ) {
                DimmedIcon(
                    icon(R.drawable.ic_close),
                    contentDescription = "Clear"
                )
            }
        }
    }
}