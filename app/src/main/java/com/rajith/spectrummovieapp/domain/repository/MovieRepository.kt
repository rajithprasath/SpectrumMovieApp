package com.rajith.spectrummovieapp.domain.repository

import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.MoviesResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovies(pageNumber: Int, category : String): Flow<Resource<MoviesResponse>>

    fun getAllGenres(): Flow<Resource<MoviesResponse>>

}