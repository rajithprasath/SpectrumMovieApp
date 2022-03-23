package com.rajith.spectrummovieapp.domain.use_case

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.domain.model.MoviesResponse
import com.rajith.spectrummovieapp.domain.repository.MovieDBRepository
import com.rajith.spectrummovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFavouriteMoviesUseCase (
    private val repository: MovieDBRepository
) {

    suspend  operator fun invoke(): Flow<Resource<List<Movie>>> {
        return repository.getFavoriteMovies()
    }
}