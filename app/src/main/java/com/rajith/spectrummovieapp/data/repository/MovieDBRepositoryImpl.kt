package com.rajith.spectrummovieapp.data.repository

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.data.local.MovieDao
import com.rajith.spectrummovieapp.data.remote.MovieAPI
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.domain.repository.MovieDBRepository
import com.rajith.spectrummovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class MovieDBRepositoryImpl (
    private val dao: MovieDao
) : MovieDBRepository {

    override suspend fun upsert(movie: Movie) {
        dao.upsert(movie)
    }

    override suspend fun getFavoriteMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())
        val movie: List<Movie>
        try {
            movie = dao.getFavouriteMovies()
            emit(Resource.Success(movie))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!"
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection."
                )
            )
        }
    }
}