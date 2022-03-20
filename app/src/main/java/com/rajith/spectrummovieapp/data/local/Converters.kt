package com.rajith.spectrummovieapp.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.rajith.spectrummovieapp.data.util.JsonParser
import com.rajith.spectrummovieapp.domain.model.Genre

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromMeaningsJson(json: String): List<Genre> {
        return jsonParser.fromJson<ArrayList<Genre>>(
            json,
            object : TypeToken<ArrayList<Genre>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toMeaningsJson(meanings: List<Genre>): String {
        return jsonParser.toJson(
            meanings,
            object : TypeToken<ArrayList<Genre>>(){}.type
        ) ?: "[]"
    }
}