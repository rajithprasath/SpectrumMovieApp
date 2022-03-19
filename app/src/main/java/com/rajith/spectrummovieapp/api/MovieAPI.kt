package com.rajith.spectrummovieapp.api

import com.rajith.spectrummovieapp.MovieResponse
import com.rajith.spectrummovieapp.MoviesResponse
import com.rajith.spectrummovieapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    @GET("3/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language")
        country: String = "en-US",
        @Query("page")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Response<MoviesResponse>

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("language")
        country: String = "en-US",
        @Query("page")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Response<MoviesResponse>

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language")
        country: String = "en-US",
        @Query("page")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Response<MoviesResponse>

    @GET("3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language")
        country: String = "en-US",
        @Query("page")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Response<MoviesResponse>

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
    ): Response<MoviesResponse>

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
    ): Response<MovieResponse>
}