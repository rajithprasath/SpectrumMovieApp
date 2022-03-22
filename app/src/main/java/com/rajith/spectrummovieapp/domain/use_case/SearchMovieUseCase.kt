package com.rajith.spectrummovieapp.domain.use_case

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.MoviesResponse
import com.rajith.spectrummovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchMovieUseCase (
    private val repository: MovieRepository
) {

    operator fun invoke(page: Int, searchQuery: String): Flow<Resource<MoviesResponse>> {
        if (page == 0) {
            return flow { }
        }
        return repository.searchMovies(page, searchQuery)
    }
}