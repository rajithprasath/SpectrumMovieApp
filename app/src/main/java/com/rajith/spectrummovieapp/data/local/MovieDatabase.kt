package com.rajith.spectrummovieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rajith.spectrummovieapp.domain.model.Genre
import com.rajith.spectrummovieapp.domain.model.Movie

@Database(
    entities = [Movie::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object{
        const val DATABASE_NAME = "movie_db.db"
    }
}