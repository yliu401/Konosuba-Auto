package com.yliu401.konosubaautomata.di.script

import com.yliu401.libautomata.IScreenshotService
import dagger.BindsInstance
import dagger.hilt.DefineComponent

@DefineComponent.Builder
interface ScriptComponentBuilder {
    fun screenshotService(@BindsInstance screenshotService: IScreenshotService): ScriptComponentBuilder
    fun build(): ScriptComponent
}