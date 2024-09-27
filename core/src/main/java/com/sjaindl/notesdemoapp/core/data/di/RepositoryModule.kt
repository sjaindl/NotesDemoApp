package com.sjaindl.notesdemoapp.core.data.di

import com.sjaindl.notesdemoapp.core.domain.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideNotesRepository(
        notesRepositoryImpl: com.sjaindl.notesdemoapp.core.data.NotesRepositoryImpl
    ): NotesRepository
}
