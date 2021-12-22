package com.yliu401.konosubaautomata.di.script

import com.yliu401.konosubaautomata.accessibility.AccessibilityGestures
import com.yliu401.konosubaautomata.scripts.FgoAutomataApi
import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.locations.IScriptAreaTransforms
import com.yliu401.konosubaautomata.scripts.locations.ScriptAreaTransforms
import com.yliu401.libautomata.IGestureService
import com.yliu401.libautomata.dagger.ScriptScope
import com.yliu401.libautomata.extensions.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn

@Module
@InstallIn(ScriptComponent::class)
interface ScriptBindsModule {
    @ScriptScope
    @Binds
    fun bindFgAutomataApi(fgoAutomataApi: FgoAutomataApi): IFgoAutomataApi

    @ScriptScope
    @Binds
    fun bindAutomataApi(automataApi: AutomataApi): IAutomataExtensions

    @ScriptScope
    @Binds
    fun bindGestureExtensions(gestureExtensions: GestureExtensions): IGestureExtensions

    @ScriptScope
    @Binds
    fun bindHighlightExtensions(highlightExtensions: HighlightExtensions): IHighlightExtensions

    @ScriptScope
    @Binds
    fun bindImageMatchingExtensions(imageMatchingExtensions: ImageMatchingExtensions): IImageMatchingExtensions

    @ScriptScope
    @Binds
    fun bindTransformationExtensions(transformationExtensions: TransformationExtensions): ITransformationExtensions

    @ScriptScope
    @Binds
    fun bindGestures(gestures: AccessibilityGestures): IGestureService

    @ScriptScope
    @Binds
    fun bindDurationExtensions(durationExtensions: DurationExtensions): IDurationExtensions

    @ScriptScope
    @Binds
    fun bindScriptAreaTransforms(scriptAreaTransforms: ScriptAreaTransforms): IScriptAreaTransforms
}