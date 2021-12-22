package com.yliu401.konosubaautomata.scripts.modules

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject
import kotlin.time.Duration

@ScriptScope
class ConnectionRetry @Inject constructor(
    api: IFgoAutomataApi
) : IFgoAutomataApi by api {
    fun needsToRetry() =
        images[Images.Retry] in locations.retryRegion

    fun retry() {
        locations.retryRegion.click()

        Duration.seconds(2).wait()
    }
}