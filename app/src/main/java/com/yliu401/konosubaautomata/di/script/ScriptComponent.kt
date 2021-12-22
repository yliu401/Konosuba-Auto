package com.yliu401.konosubaautomata.di.script

import com.yliu401.libautomata.dagger.ScriptScope
import dagger.hilt.DefineComponent
import dagger.hilt.android.components.ServiceComponent

@ScriptScope
@DefineComponent(parent = ServiceComponent::class)
interface ScriptComponent