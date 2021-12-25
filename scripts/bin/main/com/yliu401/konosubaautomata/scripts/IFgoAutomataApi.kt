package com.yliu401.konosubaautomata.scripts

import com.yliu401.konosubaautomata.scripts.locations.Locations
import com.yliu401.konosubaautomata.scripts.prefs.IPreferences
import com.yliu401.libautomata.extensions.IAutomataExtensions

interface IFgoAutomataApi : IAutomataExtensions {
    val prefs: IPreferences
    val images: IImageLoader
    val locations: Locations
    val messages: IScriptMessages
}