package com.rajith.spectrummovieapp.domain.use_case

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.domain.repository.MovieDatabaseRepository
import kotlinx.coroutines.flow.Flow

class GetFavouriteMoviesUseCase (
    private val repository: MovieDatabaseRepository
) {

    suspend  operator fun invoke(): Flow<Resource<List<Movie>>> {
        return repository.getFavoriteMovies()
    }
}