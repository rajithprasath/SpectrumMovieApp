package com.rajith.spectrummovieapp.di

import com.rajith.spectrummovieapp.data.remote.MovieAPI
import com.rajith.spectrummovieapp.core.util.Constants
import com.rajith.spectrummovieapp.data.repository.MovieNetworkRepositoryImpl
import com.rajith.spectrummovieapp.domain.repository.MovieNetworkRepository
import com.rajith.spectrummovieapp.domain.use_case.GetGenresUseCase
import com.rajith.spectrummovieapp.domain.use_case.GetMovieDetailUseCase
import com.rajith.spectrummovieapp.domain.use_case.GetMoviesUseCase
import com.rajith.spectrummovieapp.domain.use_case.SearchMovieUseCase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideGetMoviesUseCase(networkRepository: MovieNetworkRepository): GetMoviesUseCase {
        return GetMoviesUseCase(networkRepository)
    }

    @Provides
    @Singleton
    fun provideSearchMovieUseCase(networkRepository: MovieNetworkRepository): SearchMovieUseCase {
        return SearchMovieUseCase(networkRepository)
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailUseCase(networkRepository: MovieNetworkRepository): GetMovieDetailUseCase {
        return GetMovieDetailUseCase(networkRepository)
    }

    @Provides
    @Singleton
    fun provideGetGenresUseCase(networkRepository: MovieNetworkRepository): GetGenresUseCase {
        return GetGenresUseCase(networkRepository)
    }

    @Provides
    @Singleton
    fun provideMovieNetworkRepository(
        api: MovieAPI
    ): MovieNetworkRepository {
        return MovieNetworkRepositoryImpl(api)
    }

}