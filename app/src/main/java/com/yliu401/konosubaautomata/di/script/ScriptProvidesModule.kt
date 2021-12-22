package com.yliu401.konosubaautomata.di.script

import com.yliu401.konosubaautomata.scripts.FgoGameAreaManager
import com.yliu401.konosubaautomata.scripts.models.AutoSkillCommand
import com.yliu401.konosubaautomata.scripts.models.CardPriorityPerWave
import com.yliu401.konosubaautomata.scripts.models.ServantPriorityPerWave
import com.yliu401.konosubaautomata.scripts.models.SpamConfigPerTeamSlot
import com.yliu401.konosubaautomata.scripts.prefs.IBattleConfig
import com.yliu401.konosubaautomata.scripts.prefs.IPreferences
import com.yliu401.konosubaautomata.scripts.prefs.ISupportPreferences
import com.yliu401.konosubaautomata.scripts.prefs.isNewUI
import com.yliu401.libautomata.ExitManager
import com.yliu401.libautomata.GameAreaManager
import com.yliu401.libautomata.IPlatformImpl
import com.yliu401.libautomata.dagger.ScriptScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

@Module
@InstallIn(ScriptComponent::class)
class ScriptProvidesModule {
    @ScriptScope
    @Provides
    fun provideExitManager() = ExitManager()

    @ScriptScope
    @Provides
    fun provideGameAreaManager(platformImpl: IPlatformImpl, prefs: IPreferences): GameAreaManager =
        FgoGameAreaManager(
            gameSizeWithBorders = platformImpl.windowRegion.size,
            offset = { platformImpl.windowRegion.location },
            isNewUI = prefs.isNewUI
        )

    @ScriptScope
    @Provides
    fun provideBattleConfig(prefs: IPreferences): IBattleConfig =
        prefs.selectedBattleConfig

    @ScriptScope
    @Provides
    fun provideSupportPrefs(battleConfig: IBattleConfig): ISupportPreferences =
        battleConfig.support

    @ScriptScope
    @Provides
    fun provideSpamConfig(battleConfig: IBattleConfig): SpamConfigPerTeamSlot =
        SpamConfigPerTeamSlot(battleConfig.spam)

    @ScriptScope
    @Provides
    fun provideSkillCommand(battleConfig: IBattleConfig): AutoSkillCommand =
        AutoSkillCommand.parse(battleConfig.skillCommand)

    @ScriptScope
    @Provides
    fun provideCardPriority(battleConfig: IBattleConfig): CardPriorityPerWave =
        battleConfig.cardPriority

    @ScriptScope
    @Provides
    fun provideServantPriority(battleConfig: IBattleConfig): ServantPriorityPerWave? =
        if (battleConfig.useServantPriority) battleConfig.servantPriority else null
}