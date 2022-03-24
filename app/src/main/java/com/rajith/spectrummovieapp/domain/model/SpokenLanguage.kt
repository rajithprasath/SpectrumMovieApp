package com.rajith.spectrummovieapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "languages"
)
data class SpokenLanguage(
    @PrimaryKey
    val id: Int?,
    val english_name: String?,
    val iso_639_1: String?,
    val name: String?
) {

}