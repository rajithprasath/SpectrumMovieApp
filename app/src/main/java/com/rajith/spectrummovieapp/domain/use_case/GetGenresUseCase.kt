package com.rajith.spectrummovieapp.domain.use_case

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.GenreResponse
import com.rajith.spectrummovieapp.domain.repository.MovieNetworkRepository
import kotlinx.coroutines.flow.Flow

class GetGenresUseCase(
    private val networkRepository: MovieNetworkRepository
) {

    operator fun invoke(): Flow<Resource<GenreResponse>> {
        return networkRepository.getAllGenres()
    }
}