package com.rajith.spectrummovieapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.MoviesResponse
import com.rajith.spectrummovieapp.domain.use_case.SearchMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase
) : ViewModel() {

    val searchMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    private var searchMoviePage = 1
    private var searchMovieJob: Job? = null

    fun searchMovies(searchQuery: String) {
        searchMovieJob?.cancel()
        searchMovieJob = viewModelScope.launch {
            searchMovieUseCase(searchMoviePage, searchQuery)
                .onEach { result ->
                    handleResponse(result, searchMovies)
                }.launchIn(this)
        }
    }

    private fun handleResponse(result: Resource<MoviesResponse>, mutableData: MutableLiveData<Resource<MoviesResponse>>){
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