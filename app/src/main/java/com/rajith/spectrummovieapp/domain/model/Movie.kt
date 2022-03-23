package com.rajith.spectrummovieapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies"
)
data class Movie(
    @PrimaryKey
    val id: Int,
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>? = null,
    var genres: List<Genre>,
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

object MovieMapper {

    fun Movie.fillGenre(genres: List<Genre>): Movie {
        fillGenreList(genres)
        return Movie(
            id = this.id,
            poster_path = this.poster_path,
            title = this.title,
            vote_average = this.vote_average,
            vote_count = this.vote_count,
            release_date = this.release_date,
            backdrop_path = this.backdrop_path,
            genres = this.genres,
            video = this.video,
            adult = this.adult,
            original_title = this.original_title,
            original_language = this.original_language,
            popularity = this.popularity,
            overview = this.overview
        )
    }

    private fun Movie.fillGenreList(genres: List<Genre>) {
        val genreList = mutableListOf<Genre>()
        this.genre_ids?.forEach { id ->
            genres.find { it.id == id }?.apply {
                genreList.add(this)
            }
        }
        this.genres = genreList
    }
}
