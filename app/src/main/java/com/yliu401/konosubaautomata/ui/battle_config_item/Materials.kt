package com.yliu401.konosubaautomata.ui.battle_config_item

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yliu401.konosubaautomata.R
import com.yliu401.konosubaautomata.prefs.core.Pref
import com.yliu401.konosubaautomata.scripts.enums.MaterialEnum
import com.yliu401.konosubaautomata.ui.FgaDialog
import com.yliu401.konosubaautomata.ui.multiChoiceList
import com.yliu401.konosubaautomata.ui.prefs.remember
import com.yliu401.konosubaautomata.util.drawable
import com.yliu401.konosubaautomata.util.stringRes

@Composable
fun Pref<Set<MaterialEnum>>.Materials() {
    var selected by remember()

    val dialog = FgaDialog()

    dialog.build {
        var current by remember(selected) { mutableStateOf(selected) }

        Row {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline()
            ) {
                title(stringResource(R.string.p_mats))
            }

            TextButton(
                onClick = { current = emptySet() },
                modifier = Modifier
                    .padding(16.dp, 5.dp)
                    .alignByBaseline()
            ) {
                // TODO: Localize
                Text("CLEAR")
            }
        }

        multiChoiceList(
            selected = current,
            onSelectedChange = { current = it },
            items = MaterialEnum.values().toList()
        ) { mat ->
            Material(mat)

            Text(
                stringResource(mat.stringRes),
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }

        buttons(
            onSubmit = { selected = current }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { dialog.show() }
            .heightIn(min = 70.dp)
            .padding(vertical = 5.dp)
    ) {
        Text(
            stringResource(R.string.p_mats).uppercase(),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 16.dp)
        )

        MaterialsSummary(materials = selected.toList())
    }
}

@Composable
fun Material(mat: MaterialEnum) {
    Image(
        painterResource(mat.drawable),
        contentDescription = stringResource(mat.stringRes),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(3.dp)
            .size(20.dp)
            .clip(CircleShape)
            .border(0.5.dp, MaterialTheme.colors.onSurface, CircleShape)
            .alpha(0.8f)
    )
}

@Composable
fun MaterialsSummary(materials: List<MaterialEnum>) {
    if (materials.isNotEmpty()) {
        LazyRow(
            contentPadding = PaddingValues(start = 16.dp, top = 5.dp, bottom = 5.dp)
        ) {
            items(materials) { mat ->
                Material(mat)
            }
        }
    }
    else {
        Text(
            "--",
            modifier = Modifier.padding(16.dp, 5.dp)
        )
    }
}