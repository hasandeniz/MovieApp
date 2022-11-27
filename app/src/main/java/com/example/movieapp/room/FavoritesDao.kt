package com.example.movieapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.movieapp.data.model.Movie

@Dao
interface FavoritesDao {

    @Insert
    suspend fun addMovieToFavorites(movie: Movie)

    @Query("SELECT * FROM movies WHERE imdbID = :id")
    suspend fun getFavoriteMovieByImdbId(id: String): Movie

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun removeMovieFromFavorites(id: Int)

    @Query("DELETE FROM movies")
    suspend fun removeAllMoviesFromFavorites()

    @Query("SELECT * FROM movies")
    suspend fun getAllFavoriteMovies() : List<Movie>

}