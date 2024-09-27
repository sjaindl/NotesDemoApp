package com.sjaindl.notesdemoapp.data.di

import com.sjaindl.notesdemoapp.data.SyncNotesRepositoryImpl
import com.sjaindl.notesdemoapp.domain.SyncNotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideSyncNotesRepository(
        syncNotesRepositoryImpl: SyncNotesRepositoryImpl
    ): SyncNotesRepository
}
