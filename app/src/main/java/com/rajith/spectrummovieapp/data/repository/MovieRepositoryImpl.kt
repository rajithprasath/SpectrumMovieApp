package com.rajith.spectrummovieapp.data.repository

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.data.local.MovieDao
import com.rajith.spectrummovieapp.data.remote.MovieAPI
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.domain.model.MoviesResponse
import com.rajith.spectrummovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class MovieRepositoryImpl(
    private val api: MovieAPI
    ) : MovieRepository {

    override fun getMovies(pageNumber: Int, category: String): Flow<Resource<MoviesResponse>> = flow {
        emit(Resource.Loading())
        val movies: MoviesResponse
        try {
            movies = api.getMovies(pageNumber = pageNumber, list = category)
            emit(Resource.Success(movies))
        } catch(e: HttpException) {
            emit(Resource.Error(
                message = "Oops, something went wrong!"
            ))
        } catch(e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection."
            ))
        }
    }

    override fun getMovieDetail(movieId: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading())
        val movie: Movie
        try {
            movie = api.getMovieDetails(movieId = movieId)
            emit(Resource.Success(movie))
        } catch(e: HttpException) {
            emit(Resource.Error(
                message = "Oops, something went wrong!"
            ))
        } catch(e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection."
            ))
        }
    }

    override fun searchMovies(pageNumber: Int, query: String): Flow<Resource<MoviesResponse>> = flow {
        emit(Resource.Loading())
        val movies: MoviesResponse
        try {
            movies = api.searchForMovies(pageNumber = pageNumber, searchQuery = query)
            emit(Resource.Success(movies))
        } catch(e: HttpException) {
            emit(Resource.Error(
                message = "Oops, something went wrong!"
            ))
        } catch(e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection."
            ))
        }
    }



    override fun getAllGenres(): Flow<Resource<MoviesResponse>> {
        TODO("Not yet implemented")
    }

}