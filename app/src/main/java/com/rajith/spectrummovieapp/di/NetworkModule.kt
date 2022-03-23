package com.rajith.spectrummovieapp.di

import android.content.Context
import androidx.room.Room
import com.rajith.spectrummovieapp.data.remote.MovieAPI
import com.rajith.spectrummovieapp.core.util.Constants
import com.rajith.spectrummovieapp.data.local.MovieDao
import com.rajith.spectrummovieapp.data.local.MovieDatabase
import com.rajith.spectrummovieapp.data.repository.MovieRepositoryImpl
import com.rajith.spectrummovieapp.domain.repository.MovieRepository
import com.rajith.spectrummovieapp.domain.use_case.GetMovieDetailUseCase
import com.rajith.spectrummovieapp.domain.use_case.GetMoviesUseCase
import com.rajith.spectrummovieapp.domain.use_case.SearchMovieUseCase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun providesApi(retrofit: Retrofit): MovieAPI {
        return retrofit.create(MovieAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideGetMoviesUseCase(repository: MovieRepository): GetMoviesUseCase {
        return GetMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchMovieUseCase(repository: MovieRepository): SearchMovieUseCase {
        return SearchMovieUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailUseCase(repository: MovieRepository): GetMovieDetailUseCase {
        return GetMovieDetailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: MovieAPI
    ): MovieRepository {
        return MovieRepositoryImpl(api)
    }

}