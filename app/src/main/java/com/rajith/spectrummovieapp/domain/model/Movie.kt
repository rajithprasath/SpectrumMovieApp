package com.rajith.spectrummovieapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rajith.spectrummovieapp.domain.model.Genre

@Entity(
    tableName = "movies"
)
data class Movie(
    @PrimaryKey
    val id: Int,
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val genres: List<Genre>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)