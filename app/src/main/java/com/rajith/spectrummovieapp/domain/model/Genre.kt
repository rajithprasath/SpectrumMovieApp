package com.rajith.spectrummovieapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "genres"
)
data class Genre(
    @PrimaryKey
    val id: Int,
    val name: String
)