package com.rajith.spectrummovieapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rajith.spectrummovieapp.models.Genre

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(genre: Genre):Long

    @Query("SELECT * FROM genres")
    fun getAllGenres(): LiveData<List<Genre>>
}