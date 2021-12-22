package com.yliu401.konosubaautomata.runner

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import com.yliu401.konosubaautomata.imaging.MediaProjectionScreenshotService
import com.yliu401.konosubaautomata.root.RootScreenshotService
import com.yliu401.konosubaautomata.scripts.prefs.IPreferences
import com.yliu401.konosubaautomata.scripts.prefs.wantsMediaProjectionToken
import com.yliu401.konosubaautomata.util.DisplayHelper
import com.yliu401.konosubaautomata.util.StorageProvider
import com.yliu401.konosubaautomata.util.makeLandscape
import com.yliu401.libautomata.ColorManager
import com.yliu401.libautomata.IScreenshotService
import dagger.hilt.android.scopes.ServiceScoped
import timber.log.Timber
import timber.log.error
import javax.inject.Inject
import javax.inject.Provider

@ServiceScoped
class ScreenshotServiceHolder @Inject constructor(
    private val prefs: IPreferences,
    private val storageProvider: StorageProvider,
    private val display: DisplayHelper,
    private val colorManager: ColorManager,
    private val rootScreenshotServiceProvider: Provider<RootScreenshotService>,
    private val mediaProjectionManager: MediaProjectionManager
) : AutoCloseable {
    var screenshotService: IScreenshotService? = null
        private set

    fun prepareScreenshotService() {
        screenshotService = try {
            if (prefs.wantsMediaProjectionToken) {
                // Cloning the Intent allows reuse.
                // Otherwise, the Intent gets consumed and MediaProjection cannot be started multiple times.
                val token = ScriptRunnerService.mediaProjectionToken?.clone() as Intent

                val mediaProjection =
                    mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, token)
                MediaProjectionScreenshotService(
                    mediaProjection!!,
                    display.metrics.makeLandscape(),
                    storageProvider,
                    colorManager
                )
            } else rootScreenshotServiceProvider.get()
        } catch (e: Exception) {
            Timber.error(e) { "Error preparing screenshot service" }
            null
        }
    }

    override fun close() {
        screenshotService?.close()
        screenshotService = null
    }
}