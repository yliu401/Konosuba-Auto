package com.yliu401.konosubaautomata.ui.card_priority

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.yliu401.konosubaautomata.R
import com.yliu401.konosubaautomata.scripts.enums.CardAffinityEnum
import com.yliu401.konosubaautomata.scripts.enums.CardTypeEnum
import com.yliu401.konosubaautomata.scripts.models.CardScore
import com.yliu401.konosubaautomata.ui.drag_sort.DragSort
import com.yliu401.konosubaautomata.ui.drag_sort.DragSortAdapter

@Composable
fun CardPriorityDragSort(
    scores: MutableList<CardScore>
) {
    val context = LocalContext.current

    DragSort(
        items = scores,
        viewConfigGrabber = {
            DragSortAdapter.ItemViewConfig(
                foregroundColor = Color.WHITE,
                backgroundColor = context.getColor(it.getColorRes()),
                text = it.toString()
            )
        }
    )
}

fun CardScore.getColorRes(): Int {
    return when (type) {
        CardTypeEnum.Buster -> when (affinity) {
            CardAffinityEnum.Weak -> R.color.colorBusterWeak
            CardAffinityEnum.Normal -> R.color.colorBuster
            CardAffinityEnum.Resist -> R.color.colorBusterResist
        }
        CardTypeEnum.Arts -> when (affinity) {
            CardAffinityEnum.Weak -> R.color.colorArtsWeak
            CardAffinityEnum.Normal -> R.color.colorArts
            CardAffinityEnum.Resist -> R.color.colorArtsResist
        }
        CardTypeEnum.Quick -> when (affinity) {
            CardAffinityEnum.Weak -> R.color.colorQuickWeak
            CardAffinityEnum.Normal -> R.color.colorQuick
            CardAffinityEnum.Resist -> R.color.colorQuickResist
        }
        CardTypeEnum.Unknown -> R.color.colorPrimaryDark
    }
}