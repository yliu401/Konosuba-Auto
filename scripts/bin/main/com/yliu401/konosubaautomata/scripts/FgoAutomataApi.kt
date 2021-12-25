package com.yliu401.konosubaautomata.scripts

import com.yliu401.konosubaautomata.scripts.locations.Locations
import com.yliu401.konosubaautomata.scripts.prefs.IPreferences
import com.yliu401.libautomata.extensions.IAutomataExtensions
import javax.inject.Inject

class FgoAutomataApi @Inject constructor(
    automataApi: IAutomataExtensions,
    override val prefs: IPreferences,
    override val images: IImageLoader,
    override val locations: Locations,
    override val messages: IScriptMessages
) : IFgoAutomataApi, IAutomataExtensions by automataApi