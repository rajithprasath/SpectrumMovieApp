package com.rajith.spectrummovieapp.di

import android.content.Context
import androidx.room.Room
import com.rajith.spectrummovieapp.data.local.MovieDatabase
import com.rajith.spectrummovieapp.data.local.MovieDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}