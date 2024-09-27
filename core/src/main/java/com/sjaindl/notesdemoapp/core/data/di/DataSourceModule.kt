package com.sjaindl.notesdemoapp.core.data.di

import com.sjaindl.notesdemoapp.core.data.db.NotesDatabaseDataSource
import com.sjaindl.notesdemoapp.core.data.db.NotesDatabaseDataSourceImpl
import com.sjaindl.notesdemoapp.core.data.di.qualifiers.NotesRemoteApiDataSource
import com.sjaindl.notesdemoapp.core.data.di.qualifiers.NotesRemoteDataSourceQualifier
import com.sjaindl.notesdemoapp.core.data.file.NotesFileDataSource
import com.sjaindl.notesdemoapp.core.data.file.NotesFileDataSourceImpl
import com.sjaindl.notesdemoapp.core.data.remote.NotesRemoteApi
import com.sjaindl.notesdemoapp.core.data.remote.NotesRemoteDataSource
import com.sjaindl.notesdemoapp.core.data.remote.NotesRemoteRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    internal abstract fun provideNotesDatabaseDataSource(
        notesDatabaseDataSourceImpl: NotesDatabaseDataSourceImpl
    ): NotesDatabaseDataSource


    @Binds
    @NotesRemoteDataSourceQualifier
    internal abstract fun provideNotesRemoteDataSource(
        notesDatabaseDataSourceImpl: NotesRemoteRemoteDataSourceImpl
    ): NotesRemoteDataSource

    @Binds
    internal abstract fun provideNotesFileDataSource(
        notesDatabaseDataSourceImpl: NotesFileDataSourceImpl
    ): NotesFileDataSource

    @Binds
    @NotesRemoteApiDataSource
    internal abstract fun provideNotesRemoteApi(
        notesRemoteApi: NotesRemoteApi
    ): NotesRemoteDataSource
}
