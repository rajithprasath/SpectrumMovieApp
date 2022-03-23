package com.rajith.spectrummovieapp.domain.use_case

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.MoviesResponse
import com.rajith.spectrummovieapp.domain.repository.MovieNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMoviesUseCase(
    private val networkRepository: MovieNetworkRepository
) {

    operator fun invoke(page: Int, movieType: String): Flow<Resource<MoviesResponse>> {
        if (page == 0) {
            return flow { }
        }
        return networkRepository.getMovies(page, movieType)
    }
}