package com.rajith.spectrummovieapp.data.repository

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.data.remote.MovieAPI
import com.rajith.spectrummovieapp.domain.model.MoviesResponse
import com.rajith.spectrummovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MovieRepositoryImpl(
    private val api: MovieAPI) : MovieRepository {
    override fun getMovies(pageNumber: Int, category: String): Flow<Resource<MoviesResponse>> = flow {
        emit(Resource.Loading())
        val nowPlayingMovies: MoviesResponse
        try {
            nowPlayingMovies = api.getMovies(pageNumber = pageNumber, list = category)
            emit(Resource.Success(nowPlayingMovies))
        } catch(e: HttpException) {
            emit(Resource.Error(
                message = "Oops, something went wrong!"
            ))
        } catch(e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection."
            ))
        }
    } as Flow<Resource<MoviesResponse>>


    override fun getAllGenres(): Flow<Resource<MoviesResponse>> {
        TODO("Not yet implemented")
    }

}