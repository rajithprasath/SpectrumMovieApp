package com.rajith.spectrummovieapp.domain.use_case

import com.rajith.spectrummovieapp.domain.repository.MovieDatabaseRepository

class CheckMovieExistUseCase (
    private val repository: MovieDatabaseRepository
) {

    suspend  operator fun invoke(movieId: Int) : Boolean {
        return repository.exists(movieId)
    }
}