package com.rajith.spectrummovieapp.domain.use_case
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.domain.repository.MovieDBRepository
import com.rajith.spectrummovieapp.domain.repository.MovieRepository

class SaveMovieUseCase (
    private val repository: MovieDBRepository
) {

    suspend  operator fun invoke(movie: Movie) {
        return repository.upsert(movie)
    }
}