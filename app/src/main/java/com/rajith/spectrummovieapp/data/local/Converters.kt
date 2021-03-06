package com.rajith.spectrummovieapp.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.rajith.spectrummovieapp.data.util.JsonParser
import com.rajith.spectrummovieapp.domain.model.Genre
import com.rajith.spectrummovieapp.domain.model.SpokenLanguage

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {


    @TypeConverter
    fun fromGenreJson(json: String): List<Genre> {
        return jsonParser.fromJson<ArrayList<Genre>>(
            json,
            object : TypeToken<ArrayList<Genre>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toGenreJson(genres: List<Genre>): String {
        return jsonParser.toJson(
            genres,
            object : TypeToken<ArrayList<Genre>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromGenreIdJson(json: String): List<Int> {
        return jsonParser.fromJson<ArrayList<Int>>(
            json,
            object : TypeToken<ArrayList<Int>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toGenreIdJson(genreIds: List<Int>?): String {
        genreIds?.let {
            return jsonParser.toJson(
                genreIds,
                object : TypeToken<ArrayList<Int>>(){}.type
            ) ?: "[]"
        }
        return "[]"
    }

    @TypeConverter
    fun fromLanguageJson(json: String): List<SpokenLanguage> {
        return jsonParser.fromJson<ArrayList<SpokenLanguage>>(
            json,
            object : TypeToken<ArrayList<SpokenLanguage>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toLanguageJson(languages: List<SpokenLanguage>): String {
        return jsonParser.toJson(
            languages,
            object : TypeToken<ArrayList<SpokenLanguage>>(){}.type
        ) ?: "[]"
    }
}