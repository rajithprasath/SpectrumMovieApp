package com.rajith.spectrummovieapp.domain.model

data class MoviesResponse(
    val dates: Dates,
    val page: Int,
    val results: MutableList<Movie>,
    val total_pages: Int,
    val total_results: Int
)