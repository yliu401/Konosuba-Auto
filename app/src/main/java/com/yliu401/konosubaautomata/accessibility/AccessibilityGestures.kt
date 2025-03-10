package com.yliu401.konosubaautomata.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Build
import androidx.annotation.RequiresApi
import com.yliu401.konosubaautomata.scripts.prefs.IGesturesPreferences
import com.yliu401.libautomata.IGestureService
import com.yliu401.libautomata.Location
import com.yliu401.libautomata.extensions.IDurationExtensions
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import timber.log.debug
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.math.*

/**
 * Class to perform gestures using Android's [AccessibilityService].
 */
class AccessibilityGestures @Inject constructor(
    private val gesturePrefs: IGesturesPreferences,
    durationExtensions: IDurationExtensions
) : IGestureService, IDurationExtensions by durationExtensions {
    fun Path.moveTo(location: Location) = apply {
        moveTo(location.x.toFloat(), location.y.toFloat())
    }

    fun Path.lineTo(location: Location) = apply {
        lineTo(location.x.toFloat(), location.y.toFloat())
    }

    /**
     * On Android 7, swipe is like a flick.
     * If the swipe distance is too long, FGO won't detect it correctly and have occasional weird behaviour like sudden jumps
     */
    suspend fun swipe7(start: Location, end: Location) {
        val swipePath = Path()
            .moveTo(start)
            .lineTo(end)

        val swipeStroke = GestureDescription.StrokeDescription(
            swipePath,
            0,
            gesturePrefs.swipeDuration.inWholeMilliseconds
        )
        performGesture(swipeStroke)

        gesturePrefs.swipeWaitTime.wait()
    }

    /**
     * Android 8+ swipe is precise due to use of continued gestures.
     *
     * Instead of swiping the whole distance as a single gesture,
     * it is split into multiple small swipes, which is similar to how events are sent if a real human is doing it.
     * There is a finger down delay, followed by multiple small swipe events, followed by a finger lift delay.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun swipe8(start: Location, end: Location) {
        val xDiff = (end.x - start.x).toFloat()
        val yDiff = (end.y - start.y).toFloat()
        val direction = atan2(xDiff, yDiff)
        var distanceLeft = sqrt(xDiff.pow(2) + yDiff.pow(2))

        val swipeDelay = 1L
        val swipeDuration = 1L

        val timesToSwipe = gesturePrefs.swipeDuration.inWholeMilliseconds / (swipeDelay + swipeDuration)
        val thresholdDistance = distanceLeft / timesToSwipe

        var from = start
        val mouseDownPath = Path().moveTo(start)

        var lastStroke = GestureDescription.StrokeDescription(
            mouseDownPath,
            0,
            200L,
            true
        ).also {
            performGesture(it)
        }

        while (distanceLeft > 0) {
            val distanceToScroll = minOf(thresholdDistance, distanceLeft)

            val x = (from.x + distanceToScroll * sin(direction)).roundToInt()
            val y = (from.y + distanceToScroll * cos(direction)).roundToInt()
            val to = Location(x, y)

            val swipePath = Path()
                .moveTo(from)
                .lineTo(to)

            lastStroke = lastStroke.continueStroke(
                swipePath,
                swipeDelay,
                swipeDuration,
                true
            ).also {
                performGesture(it)
            }

            from = to
            distanceLeft -= distanceToScroll
        }

        val mouseUpPath = Path().moveTo(from)

        lastStroke.continueStroke(
            mouseUpPath,
            1,
            400L,
            false
        ).also {
            performGesture(it)
        }
    }

    override fun swipe(start: Location, end: Location) = runBlocking {
        Timber.debug { "swipe $start, $end" }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            swipe8(start, end)
        } else swipe7(start, end)
    }

    override fun click(location: Location, times: Int) = runBlocking {
        val swipePath = Path().moveTo(location)

        val stroke = GestureDescription.StrokeDescription(
            swipePath,
            gesturePrefs.clickDelay.inWholeMilliseconds,
            gesturePrefs.clickDuration.inWholeMilliseconds
        )

        Timber.debug { "click $location x$times" }

        repeat(times) {
            performGesture(stroke)
        }

        gesturePrefs.clickWaitTime.wait()
    }

    private suspend fun performGesture(StrokeDesc: GestureDescription.StrokeDescription): Boolean = suspendCancellableCoroutine {
        val gestureDesc = GestureDescription.Builder()
            .addStroke(StrokeDesc)
            .build()

        val callback = object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                it.resume(true)
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                it.resume(false)
            }
        }

        TapperService.instance?.dispatchGesture(gestureDesc, callback, null)
    }

    override fun close() {}
}