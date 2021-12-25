package com.yliu401.konosubaautomata.ui.launcher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yliu401.konosubaautomata.R
import com.yliu401.konosubaautomata.scripts.prefs.IPreferences
import com.yliu401.konosubaautomata.ui.Stepper

@Composable//should be just a clickable button to run.
fun MSLauncher(
    prefs: IPreferences,
    modifier: Modifier = Modifier
): ScriptLauncherResponseBuilder {
    var shouldLimit by remember { mutableStateOf(prefs.shouldLimitFP) }
    var rollLimit by remember { mutableStateOf(prefs.limitFP) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 5.dp)
    ) {
        Text(
            stringResource(R.string.p_script_mode_ms),
            style = MaterialTheme.typography.h6
        )

        Divider(
            modifier = Modifier
                .padding(5.dp)
                .padding(bottom = 16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { shouldLimit = !shouldLimit }
        ) {
            Text(
                stringResource(R.string.p_ms2),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary
            )

            Switch(
                checked = shouldLimit,
                onCheckedChange = { shouldLimit = it }
            )
        }

        Box(
            modifier = Modifier.align(Alignment.End)
        ) {
            Stepper(
                value = rollLimit,
                onValueChange = { rollLimit = it },
                valueRange = 1..999,
                enabled = shouldLimit
            )
        }
    }

    return ScriptLauncherResponseBuilder(
        canBuild = { true },
        build = {
            ScriptLauncherResponse.MainStory(
                if (shouldLimit) true else false
            )
        }

    )
}