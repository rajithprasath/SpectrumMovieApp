package com.rajith.spectrummovieapp.domain.use_case
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.domain.repository.MovieDatabaseRepository

class SaveMovieUseCase (
    private val repository: MovieDatabaseRepository
) {

    suspend  operator fun invoke(movie: Movie) {
        return repository.upsert(movie)
    }
}