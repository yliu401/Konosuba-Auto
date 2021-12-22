package com.yliu401.konosubaautomata.di.app

import com.yliu401.konosubaautomata.IStorageProvider
import com.yliu401.konosubaautomata.prefs.PreferencesImpl
import com.yliu401.konosubaautomata.scripts.IImageLoader
import com.yliu401.konosubaautomata.scripts.prefs.IPreferences
import com.yliu401.konosubaautomata.util.ImageLoader
import com.yliu401.konosubaautomata.util.StorageProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppBindsModule {
    @Singleton
    @Binds
    fun bindImageLoader(imageLoader: ImageLoader): IImageLoader

    @Singleton
    @Binds
    fun bindPrefs(prefs: PreferencesImpl): IPreferences

    @Singleton
    @Binds
    fun bindStorageProvider(storageProvider: StorageProvider): IStorageProvider
}