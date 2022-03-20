package com.rajith.spectrummovieapp.data.local

import androidx.room.TypeConverter
import com.rajith.spectrummovieapp.domain.model.Genre
import com.squareup.moshi.Moshi

class Converters {

    @TypeConverter
    fun fromGenre(genre: Genre): String = toJson(genre)

    @TypeConverter
    fun toGenre(id: Int, name: String): Genre {
        return Genre(id, name)
    }

    inline fun <reified Genre> toJson(value: Genre): String {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(Genre::class.java)
        return jsonAdapter.toJson(value)
    }
}