package com.yliu401.konosubaautomata.ui.more

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.yliu401.konosubaautomata.R
import com.yliu401.konosubaautomata.ui.icon
import com.yliu401.konosubaautomata.ui.prefs.Preference

@Composable
fun StorageGroup(
    directoryName: String,
    onPickDirectory: () -> Unit,
    extractSupportImages: () -> Unit,
    extractSummary: String
) {
    Preference(
        title = stringResource(R.string.p_folder),
        summary = directoryName,
        icon = icon(R.drawable.ic_folder_edit),
        onClick = onPickDirectory
    )

    Preference(
        title = stringResource(R.string.support_menu_extract_default_support_images),
        icon = icon(Icons.Default.Image),
        onClick = extractSupportImages,
        summary = extractSummary
    )
}