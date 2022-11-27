package com.example.movieapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class Movie(
    @ColumnInfo(name = "Title")
    @SerializedName("Title")
    val title: String,
    @ColumnInfo(name = "Year")
    @SerializedName("Year")
    val year: String,
    @ColumnInfo(name = "imdbID")
    @SerializedName("imdbID")
    val imdbId: String,
    @ColumnInfo(name = "Type")
    @SerializedName("Type")
    val type: String,
    @ColumnInfo(name = "Poster")
    @SerializedName("Poster")
    val posterPath: String,

){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

