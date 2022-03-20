package com.rajith.spectrummovieapp.models

data class MoviesResponse(
    val dates: Dates,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)