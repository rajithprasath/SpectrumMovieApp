package com.rajith.spectrummovieapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var instance: MovieDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{
                instance = it
            }
        }

       private fun createDatabase(context: Context) =
           Room.databaseBuilder(
               context.applicationContext,
               MovieDatabase::class.java,
               "movie_db.db"
           ).build()

    }
}