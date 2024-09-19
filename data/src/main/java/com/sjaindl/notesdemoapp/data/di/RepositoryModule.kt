package com.sjaindl.notesdemoapp.data.di

import com.sjaindl.notesdemoapp.data.NotesRepositoryImpl
import com.sjaindl.notesdemoapp.domain.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideNotesRepository(
        notesRepositoryImpl: NotesRepositoryImpl
    ): NotesRepository
}
