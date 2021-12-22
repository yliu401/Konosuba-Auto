package com.yliu401.konosubaautomata.scripts.modules

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.enums.SupportClass
import com.yliu401.konosubaautomata.scripts.enums.canAlsoCheckAll
import com.yliu401.konosubaautomata.scripts.prefs.ISupportPreferences
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject
import kotlin.time.Duration

@ScriptScope
class SupportClassPicker @Inject constructor(
    api: IFgoAutomataApi,
    private val supportPrefs: ISupportPreferences
) : IFgoAutomataApi by api {
    fun selectSupportClass(supportClass: SupportClass = supportPrefs.supportClass) {
        if (supportClass == SupportClass.None)
            return

        locations.support.locate(supportClass).click()

        Duration.seconds(0.5).wait()
    }

    fun shouldAlsoCheckAll() =
        supportPrefs.alsoCheckAll && supportPrefs.supportClass.canAlsoCheckAll
}