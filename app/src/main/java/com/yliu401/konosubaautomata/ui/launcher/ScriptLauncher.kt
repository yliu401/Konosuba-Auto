package com.yliu401.konosubaautomata.ui.launcher

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.yliu401.konosubaautomata.R
import com.yliu401.konosubaautomata.scripts.enums.ScriptModeEnum
import com.yliu401.konosubaautomata.scripts.prefs.IPreferences
import com.yliu401.konosubaautomata.ui.FgaScreen

@Composable
fun ScriptLauncher(
    scriptMode: ScriptModeEnum,
    onResponse: (ScriptLauncherResponse) -> Unit,
    prefs: IPreferences
) {
    FgaScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val modifier = Modifier.weight(1f)

            val responseBuilder = when (scriptMode) {
                ScriptModeEnum.Battle, ScriptModeEnum.SupportImageMaker ->
                    battleLauncher(prefs, modifier)
                ScriptModeEnum.FP -> fpLauncher(prefs, modifier)
                ScriptModeEnum.Lottery -> lotteryLauncher(prefs, modifier)
                ScriptModeEnum.PresentBox -> giftBoxLauncher(prefs, modifier)
                ScriptModeEnum.CEBomb -> ceBombLauncher(prefs, modifier)
                ScriptModeEnum.MainStory -> MSLauncher(prefs, modifier)
            }

            Divider()

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    if (scriptMode == ScriptModeEnum.SupportImageMaker) {
                        TextButton(onClick = { onResponse(ScriptLauncherResponse.SupportImageMaker) }) {
                            Text(stringResource(R.string.p_script_mode_support_image_maker))
                        }
                    }
                }

                Row {
                    TextButton(onClick = { onResponse(ScriptLauncherResponse.Cancel) }) {
                        Text(stringResource(android.R.string.cancel))
                    }

                    TextButton(
                        onClick = { onResponse(responseBuilder.build()) },
                        enabled = responseBuilder.canBuild()
                    ) {
                        Text(stringResource(android.R.string.ok))
                    }
                }
            }
        }
    }
}
