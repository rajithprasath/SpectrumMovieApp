package com.rajith.spectrummovieapp.data.remote

import com.rajith.spectrummovieapp.domain.model.GenreResponse
import com.rajith.spectrummovieapp.domain.model.MoviesResponse
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.core.util.Constants.Companion.API_KEY
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    @GET("3/movie/{list}")
    suspend fun getMovies(
        @Path("list") list: String,
        @Query("language")
        country: String = "en-US",
        @Query("page")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY
    ): MoviesResponse


    @GET("3/search/movie")
    suspend fun searchForMovies(
        @Query("language")
        country: String = "en-US",
        @Query("query")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY
    ): MoviesResponse

    @GET("3/movie")
    suspend fun getMovieDetails(
        @Path("movie_id")
        movieId: String,
        @Query("language")
        country: String = "en-US",
        @Query("query")
        searchQuery: String,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Movie

    @GET("3/genre/movie/list")
    suspend fun getGenres(
        @Query("api_key")
        apiKey: String = API_KEY
    ): GenreResponse
}