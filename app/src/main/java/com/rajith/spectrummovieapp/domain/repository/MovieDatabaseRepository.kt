package com.rajith.spectrummovieapp.domain.repository

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieDatabaseRepository {

    suspend fun upsert(movie: Movie)

    suspend fun getFavoriteMovies(): Flow<Resource<List<Movie>>>

    suspend fun exists(movieId: Int): Boolean
}