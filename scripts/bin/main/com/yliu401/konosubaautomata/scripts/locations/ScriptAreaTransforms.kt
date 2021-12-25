package com.yliu401.konosubaautomata.scripts.locations

import com.yliu401.konosubaautomata.scripts.isWide
import com.yliu401.konosubaautomata.scripts.prefs.IPreferences
import com.yliu401.konosubaautomata.scripts.prefs.isNewUI
import com.yliu401.libautomata.GameAreaManager
import com.yliu401.libautomata.IPlatformImpl
import com.yliu401.libautomata.Location
import com.yliu401.libautomata.Region
import com.yliu401.libautomata.dagger.ScriptScope
import com.yliu401.libautomata.extensions.ITransformationExtensions
import javax.inject.Inject

@ScriptScope
class ScriptAreaTransforms @Inject constructor(
    prefs: IPreferences,
    transformationExtensions: ITransformationExtensions,
    gameAreaManager: GameAreaManager,
    platformImpl: IPlatformImpl
) : IScriptAreaTransforms {
    override val scriptArea =
        Region(
            Location(),
            gameAreaManager.gameArea.size * (1 / transformationExtensions.scriptToScreenScale())
        )

    override val isWide = prefs.isNewUI && scriptArea.size.isWide()

    override val isNewUI = prefs.isNewUI

    override val gameServer = prefs.gameServer

    override val canLongSwipe = platformImpl.canLongSwipe

    override fun Location.xFromCenter() =
        this + Location(scriptArea.center.x, 0)

    override fun Region.xFromCenter() =
        this + Location(scriptArea.center.x, 0)

    override fun Location.xFromRight() =
        this + Location(scriptArea.right, 0)

    override fun Region.xFromRight() =
        this + Location(scriptArea.right, 0)

    override fun Location.yFromBottom() =
        this + Location(0, scriptArea.bottom)

    override fun Region.yFromBottom() =
        this + Location(0, scriptArea.bottom)
}