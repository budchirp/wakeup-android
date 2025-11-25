package dev.cankolay.wakeup.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cankolay.wakeup.data.repository.SettingsRepositoryImpl
import dev.cankolay.wakeup.data.repository.WakeupRepositoryImpl
import dev.cankolay.wakeup.domain.repository.SettingsRepository
import dev.cankolay.wakeup.domain.repository.WakeupRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWakeupRepository(
        impl: WakeupRepositoryImpl
    ): WakeupRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}


