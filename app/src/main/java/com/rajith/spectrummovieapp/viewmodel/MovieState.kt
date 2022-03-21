package com.rajith.spectrummovieapp.viewmodel

import com.rajith.spectrummovieapp.domain.model.Movie


data class MovieState(
    val wordInfoItems: List<Movie> = emptyList(),
    val isLoading: Boolean = false
)
