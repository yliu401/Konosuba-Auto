package com.yliu401.konosubaautomata.scripts.locations

import com.yliu401.konosubaautomata.scripts.enums.GameServerEnum
import com.yliu401.libautomata.Location
import com.yliu401.libautomata.Region

interface IScriptAreaTransforms {
    val scriptArea: Region
    val isWide: Boolean
    val isNewUI: Boolean
    val gameServer: GameServerEnum
    val canLongSwipe: Boolean
    fun Location.xFromCenter(): Location
    fun Region.xFromCenter(): Region
    fun Location.xFromRight(): Location
    fun Region.xFromRight(): Region
    fun Location.yFromBottom(): Location
    fun Region.yFromBottom(): Region
}