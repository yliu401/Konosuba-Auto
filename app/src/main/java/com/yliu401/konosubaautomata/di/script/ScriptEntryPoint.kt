package com.yliu401.konosubaautomata.di.script

import com.yliu401.konosubaautomata.scripts.entrypoints.*
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn

@EntryPoint
@InstallIn(ScriptComponent::class)
interface ScriptEntryPoint {
    fun battle(): AutoBattle
    fun fp(): AutoFriendGacha
    fun giftBox(): AutoGiftBox
    fun lottery(): AutoLottery
    fun supportImageMaker(): SupportImageMaker
    fun ceBomb(): AutoCEBomb

    fun autoDetect(): AutoDetect
    fun MainStory(): AutoMainStory //AutoMainStory.kt in entry points
}