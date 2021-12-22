package com.yliu401.konosubaautomata.scripts.modules

import com.yliu401.konosubaautomata.IStorageProvider
import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.libautomata.IPattern
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject

@ScriptScope
class ScreenshotDrops @Inject constructor(
    api: IFgoAutomataApi,
    private val storageProvider: IStorageProvider
) : IFgoAutomataApi by api {
    fun screenshotDrops() {
        if (!prefs.screenshotDrops)
            return

        val drops = mutableListOf<IPattern>()

        for (i in 0..1) {
            useColor {
                drops.add(locations.scriptArea.getPattern())
            }

            // check if we need to scroll to see more drops
            if (i == 0 && images[Images.DropScrollbar] in locations.resultDropScrollbarRegion) {
                // scroll to end
                locations.resultDropScrollEndClick.click()
            } else break
        }

        storageProvider.dropScreenshot(drops)
    }
}