package com.rajith.spectrummovieapp.domain.use_case

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.domain.repository.MovieNetworkRepository
import kotlinx.coroutines.flow.Flow

class GetMovieDetailUseCase (
    private val networkRepository: MovieNetworkRepository
) {

    operator fun invoke(movieId: Int): Flow<Resource<Movie>> {
        return networkRepository.getMovieDetail(movieId)
    }
}