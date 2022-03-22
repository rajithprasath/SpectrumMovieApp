package com.rajith.spectrummovieapp.domain.use_case

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMovieDetailUseCase (
    private val repository: MovieRepository
) {

    operator fun invoke(movieId: Int): Flow<Resource<Movie>> {
        return repository.getMovieDetail(movieId)
    }
}