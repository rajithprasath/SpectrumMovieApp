package com.rajith.spectrummovieapp.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.rajith.spectrummovieapp.data.local.Converters
import com.rajith.spectrummovieapp.data.local.MovieDatabase
import com.rajith.spectrummovieapp.data.local.MovieDatabase.Companion.DATABASE_NAME
import com.rajith.spectrummovieapp.data.repository.MovieDatabaseRepositoryImpl
import com.rajith.spectrummovieapp.data.util.GsonParser
import com.rajith.spectrummovieapp.domain.repository.MovieDatabaseRepository
import com.rajith.spectrummovieapp.domain.use_case.GetFavouriteMoviesUseCase
import com.rajith.spectrummovieapp.domain.use_case.SaveMovieUseCase
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
        ).addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun provideSaveMovieUseCase(repository: MovieDatabaseRepository): SaveMovieUseCase {
        return SaveMovieUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavouriteMoviesUseCase(repository: MovieDatabaseRepository): GetFavouriteMoviesUseCase {
        return GetFavouriteMoviesUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideMovieDatabaseRepository(
        db: MovieDatabase
    ): MovieDatabaseRepository {
        return MovieDatabaseRepositoryImpl(db.getMovieDao())
    }

}