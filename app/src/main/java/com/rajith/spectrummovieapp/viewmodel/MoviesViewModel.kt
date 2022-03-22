package com.rajith.spectrummovieapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajith.spectrummovieapp.core.util.MovieCategory
import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.domain.model.MoviesResponse
import com.rajith.spectrummovieapp.domain.use_case.GetMovieDetailUseCase
import com.rajith.spectrummovieapp.domain.use_case.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    val nowPlayingMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val popularMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val topRatedMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val upcomingMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val movie: MutableLiveData<Resource<Movie>> = MutableLiveData()

    private var nowPlayingPage = 1
    private var popularPage = 1
    private var topRatedPage = 1
    private var upcomingPage = 1
    private var getMoviesJob: Job? = null
    private var getMovieDetailJob: Job? = null

    fun getNowPlayingMovies() {
        getMoviesJob?.cancel()
        getMoviesJob = viewModelScope.launch {
            getMoviesUseCase(nowPlayingPage, MovieCategory.NOW_PLAYING.key)
                .onEach { result ->
                    handleMoviesResponse(result, nowPlayingMovies)
                }.launchIn(this)
        }
    }

    fun getPopularMovies() {
        getMoviesJob?.cancel()
        getMoviesJob = viewModelScope.launch {
            getMoviesUseCase(popularPage, MovieCategory.POPULAR.key)
                .onEach { result ->
                    handleMoviesResponse(result, popularMovies)
                }.launchIn(this)
        }
    }

    fun getTopRatedMovies() {
        getMoviesJob?.cancel()
        getMoviesJob = viewModelScope.launch {
            getMoviesUseCase(topRatedPage, MovieCategory.TOP_RATED.key)
                .onEach { result ->
                    handleMoviesResponse(result, topRatedMovies)
                }.launchIn(this)
        }
    }

    fun getUpcomingMovies() {
        getMoviesJob?.cancel()
        getMoviesJob = viewModelScope.launch {
            getMoviesUseCase(upcomingPage, MovieCategory.UPCOMING.key)
                .onEach { result ->
                    handleMoviesResponse(result, upcomingMovies)
                }.launchIn(this)
        }
    }

    fun getMovieDetail(movieId: Int) {
        getMovieDetailJob?.cancel()
        getMovieDetailJob = viewModelScope.launch {
            getMovieDetailUseCase(movieId)
                .onEach { result ->
                    handleMovieDetailResponse(result, movie)
                }.launchIn(this)
        }
    }


    private fun handleMoviesResponse(result: Resource<MoviesResponse>, mutableData: MutableLiveData<Resource<MoviesResponse>>){
        when(result) {
            is Resource.Success -> {
                mutableData.postValue(Resource.Success(result.data))
            }
            is Resource.Error -> {
                mutableData.postValue(result.message?.let { Resource.Error(it) })
            }
            is Resource.Loading -> {
                mutableData.postValue(Resource.Loading())
            }
        }
    }

    private fun handleMovieDetailResponse(result: Resource<Movie>, mutableData: MutableLiveData<Resource<Movie>>){
        when(result) {
            is Resource.Success -> {
                mutableData.postValue(Resource.Success(result.data))
            }
            is Resource.Error -> {
                mutableData.postValue(result.message?.let { Resource.Error(it) })
            }
            is Resource.Loading -> {
                mutableData.postValue(Resource.Loading())
            }
        }
    }

}