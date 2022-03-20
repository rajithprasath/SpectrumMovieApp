package com.rajith.spectrummovieapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rajith.spectrummovieapp.models.Genre
import com.rajith.spectrummovieapp.models.Result

@Database(
    entities = [Result::class, Genre::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao
    abstract fun getGenreDao(): GenreDao
}