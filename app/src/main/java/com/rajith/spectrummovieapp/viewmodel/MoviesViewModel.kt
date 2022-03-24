package com.rajith.spectrummovieapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajith.spectrummovieapp.MovieApplication
import com.rajith.spectrummovieapp.core.util.MovieCategory
import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.Genre
import com.rajith.spectrummovieapp.domain.model.GenreResponse
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.domain.model.MoviesResponse
import com.rajith.spectrummovieapp.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val saveMovieUseCase: SaveMovieUseCase,
    private val getFavouriteMoviesUseCase: GetFavouriteMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    private val checkMovieExistUseCase: CheckMovieExistUseCase,
    private val searchMovieUseCase: SearchMovieUseCase
) : ViewModel() {

    val nowPlayingMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val popularMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val topRatedMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val upcomingMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val genres: MutableLiveData<Resource<GenreResponse>> = MutableLiveData()
    val movie: MutableLiveData<Resource<Movie>> = MutableLiveData()
    val favoriteMovies: MutableLiveData<Resource<List<Movie>>> = MutableLiveData()
    val searchMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    var genreList: List<Genre>? = mutableListOf()

    var nowPlayingMoviesResponse: MoviesResponse? = null
    var popularMoviesResponse: MoviesResponse? = null
    var topRatedMoviesResponse: MoviesResponse? = null
    var upcomingMoviesResponse: MoviesResponse? = null
    var searchMoviesResponse: MoviesResponse? = null

     var nowPlayingPage = 1
     var popularPage = 1
     var topRatedPage = 1
     var upcomingPage = 1
    var searchMoviePage = 1

    private var getMoviesJob: Job? = null
    private var getMovieDetailJob: Job? = null
    private var getFavouriteMoviesJob: Job? = null
    private var getGenresJob: Job? = null
    private var saveMovieJob: Job? = null
    private var searchMovieJob: Job? = null

    fun getNowPlayingMovies() {
        getMoviesJob?.cancel()
        getMoviesJob = viewModelScope.launch {
            getMoviesUseCase(nowPlayingPage, MovieCategory.NOW_PLAYING.key)
                .onEach { result ->
                    handleMoviesResponse(result, nowPlayingMovies, MovieCategory.NOW_PLAYING.key)
                }.launchIn(this)
        }
    }

    fun getPopularMovies() {
        getMoviesJob?.cancel()
        getMoviesJob = viewModelScope.launch {
            getMoviesUseCase(popularPage, MovieCategory.POPULAR.key)
                .onEach { result ->
                    handleMoviesResponse(result, popularMovies, MovieCategory.POPULAR.key)
                }.launchIn(this)
        }
    }

    fun getTopRatedMovies() {
        getMoviesJob?.cancel()
        getMoviesJob = viewModelScope.launch {
            getMoviesUseCase(topRatedPage, MovieCategory.TOP_RATED.key)
                .onEach { result ->
                    handleMoviesResponse(result, topRatedMovies, MovieCategory.TOP_RATED.key)
                }.launchIn(this)
        }
    }

    fun getUpcomingMovies() {
        getMoviesJob?.cancel()
        getMoviesJob = viewModelScope.launch {
            getMoviesUseCase(upcomingPage, MovieCategory.UPCOMING.key)
                .onEach { result ->
                    handleMoviesResponse(result, upcomingMovies, MovieCategory.UPCOMING.key)
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

    fun saveMovie(movie: Movie) {
        saveMovieJob?.cancel()
        saveMovieJob = viewModelScope.launch {
            saveMovieUseCase(movie)
        }
    }

    fun getFavouriteMovies() {
        getFavouriteMoviesJob?.cancel()
        getFavouriteMoviesJob = viewModelScope.launch {
            getFavouriteMoviesUseCase()
                .onEach { result ->
                    handleFavouriteMoviesResponse(result, favoriteMovies)
                }.launchIn(this)
        }
    }

    fun getAllGenres() {
        getGenresJob?.cancel()
        getGenresJob = viewModelScope.launch {
            getGenresUseCase()
                .onEach { result ->
                    handleGenresResponse(result, genres)
                }.launchIn(this)
        }
    }

    fun searchMovies(searchQuery: String) {
        searchMovieJob?.cancel()
        searchMovieJob = viewModelScope.launch {
            searchMovieUseCase(searchMoviePage, searchQuery)
                .onEach { result ->
                    handleMoviesResponse(result, searchMovies, "")
                }.launchIn(this)
        }
    }


    suspend fun isMoveExist(movieId: Int): Boolean {
        return checkMovieExistUseCase(movieId)
    }

    private fun handleMoviesResponse(result: Resource<MoviesResponse>, mutableData: MutableLiveData<Resource<MoviesResponse>>, movieCategory: String){
        when(result) {
            is Resource.Success -> {
                when(movieCategory) {
                    MovieCategory.NOW_PLAYING.key ->   {
                        nowPlayingPage++
                        if(nowPlayingMoviesResponse == null) {
                            nowPlayingMoviesResponse = result.data
                        } else {
                            val oldMovies = nowPlayingMoviesResponse?.results
                            val newMovies = result.data?.results
                            newMovies?.let { oldMovies?.addAll(it) }
                        }

                        mutableData.postValue(Resource.Success(nowPlayingMoviesResponse ?: result.data))
                    }
                    MovieCategory.POPULAR.key -> {
                        popularPage++
                        if(popularMoviesResponse == null) {
                            popularMoviesResponse = result.data
                        } else {
                            val oldMovies = popularMoviesResponse?.results
                            val newMovies = result.data?.results
                            newMovies?.let { oldMovies?.addAll(it) }
                        }

                        mutableData.postValue(Resource.Success(popularMoviesResponse ?: result.data))
                    }
                    MovieCategory.TOP_RATED.key -> {
                        topRatedPage++
                        if(topRatedMoviesResponse == null) {
                            topRatedMoviesResponse = result.data
                        } else {
                            val oldMovies = topRatedMoviesResponse?.results
                            val newMovies = result.data?.results
                            newMovies?.let { oldMovies?.addAll(it) }
                        }

                        mutableData.postValue(Resource.Success(topRatedMoviesResponse ?: result.data))
                    }
                    MovieCategory.UPCOMING.key -> {
                        upcomingPage++
                        if(upcomingMoviesResponse == null) {
                            upcomingMoviesResponse = result.data
                        } else {
                            val oldMovies = upcomingMoviesResponse?.results
                            val newMovies = result.data?.results
                            newMovies?.let { oldMovies?.addAll(it) }
                        }

                        mutableData.postValue(Resource.Success(upcomingMoviesResponse ?: result.data))
                    }
                    else -> {
                        searchMoviePage++
                        if(searchMoviesResponse == null) {
                            searchMoviesResponse = result.data
                        } else {
                            val oldMovies = searchMoviesResponse?.results
                            val newMovies = result.data?.results
                            newMovies?.let { oldMovies?.addAll(it) }
                        }

                        mutableData.postValue(Resource.Success(searchMoviesResponse ?: result.data))
                    }
                }

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

    private fun handleFavouriteMoviesResponse(result: Resource<List<Movie>>, mutableData: MutableLiveData<Resource<List<Movie>>>){
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

    private fun handleGenresResponse(result: Resource<GenreResponse>, mutableData: MutableLiveData<Resource<GenreResponse>>){
        when(result) {
            is Resource.Success -> {
                genreList = result.data?.genres
                MovieApplication.genreList = genreList
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