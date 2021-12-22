package com.yliu401.konosubaautomata.di.vm

import androidx.lifecycle.SavedStateHandle
import com.yliu401.konosubaautomata.prefs.core.PrefsCore
import com.yliu401.konosubaautomata.scripts.prefs.IPreferences
import com.yliu401.konosubaautomata.ui.main.NavConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelProvidesModule {
    val SavedStateHandle.configKey: String get() =
        this[NavConstants.battleConfigIdKey]
            ?: throw kotlin.Exception("Couldn't get Battle Config key")

    @ViewModelScoped
    @Provides
    fun provideBattleConfig(
        prefs: IPreferences,
        savedState: SavedStateHandle
    ) = prefs.forBattleConfig(savedState.configKey)

    @ViewModelScoped
    @Provides
    fun provideBattleConfigCore(
        prefs: PrefsCore,
        savedState: SavedStateHandle
    ) = prefs.forBattleConfig(savedState.configKey)
}