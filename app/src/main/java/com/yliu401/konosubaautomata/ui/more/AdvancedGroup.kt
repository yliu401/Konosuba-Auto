package com.yliu401.konosubaautomata.ui.more

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.yliu401.konosubaautomata.R
import com.yliu401.konosubaautomata.prefs.core.GameAreaMode
import com.yliu401.konosubaautomata.prefs.core.PrefsCore
import com.yliu401.konosubaautomata.ui.icon
import com.yliu401.konosubaautomata.ui.prefs.ListPreference
import com.yliu401.konosubaautomata.ui.prefs.Preference
import com.yliu401.konosubaautomata.ui.prefs.SwitchPreference
import com.yliu401.konosubaautomata.ui.prefs.remember

fun LazyListScope.advancedGroup(
    prefs: PrefsCore,
    goToFineTune: () -> Unit
) {
    item {
        Preference(
            title = stringResource(R.string.p_fine_tune),
            icon = icon(R.drawable.ic_tune),
            onClick = goToFineTune
        )
    }

    item {
        prefs.debugMode.SwitchPreference(
            title = stringResource(R.string.p_debug_mode),
            summary = stringResource(R.string.p_debug_mode_summary),
            icon = icon(R.drawable.ic_bug)
        )
    }

    item {
        prefs.ignoreNotchCalculation.SwitchPreference(
            title = stringResource(R.string.p_ignore_notch),
            summary = stringResource(R.string.p_ignore_notch_summary),
            icon = icon(R.drawable.ic_notch)
        )
    }

    item {
        val rootForScreenshots by prefs.useRootForScreenshots.remember()

        prefs.recordScreen.SwitchPreference(
            title = stringResource(R.string.p_record_screen),
            summary = stringResource(R.string.p_record_screen_summary),
            icon = icon(R.drawable.ic_video),
            enabled = !rootForScreenshots
        )
    }

    item {
        prefs.useRootForScreenshots.SwitchPreference(
            title = stringResource(R.string.p_root_screenshot),
            summary = stringResource(R.string.p_root_screenshot_summary),
            icon = icon(R.drawable.ic_key)
        )
    }

    item {
        prefs.autoStartService.SwitchPreference(
            title = stringResource(R.string.p_auto_start_service),
            icon = icon(R.drawable.ic_launch)
        )
    }

    item {
        prefs.gameAreaMode.ListPreference(
            title = "Game Area Mode",
            icon = icon(Icons.Default.Fullscreen),
            entries = GameAreaMode.values().associateWith { it.name }
        )
    }

    item {
        prefs.stageCounterNew.SwitchPreference(
            title = "Thresholded stage counter detection",
            icon = icon(R.drawable.ic_counter)
        )
    }
}