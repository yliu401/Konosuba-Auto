package com.yliu401.konosubaautomata.di.service

import com.yliu401.konosubaautomata.scripts.IScriptMessages
import com.yliu401.konosubaautomata.util.AndroidImpl
import com.yliu401.konosubaautomata.util.ScriptMessages
import com.yliu401.libautomata.IPlatformImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
interface ServiceModule {
    @ServiceScoped
    @Binds
    fun bindPlatformImpl(impl: AndroidImpl): IPlatformImpl

    @ServiceScoped
    @Binds
    fun bindScriptMessages(scriptMessages: ScriptMessages): IScriptMessages
}