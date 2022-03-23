package com.rajith.spectrummovieapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rajith.spectrummovieapp.domain.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie: Movie): Long

    @Query("SELECT * FROM movies")
    suspend fun getFavouriteMovies(): List<Movie>
}