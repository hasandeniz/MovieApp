package com.example.movieapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.MovieDetailsResponse

@Dao
interface FavoritesDao {

    @Insert
    suspend fun addMovieToFavorites(movie: Movie)

    @Delete
    suspend fun removeMovieFromFavorites(movie: Movie)

    @Query("SELECT * FROM movies")
    suspend fun getAllFavoriteMovies() : List<Movie>

    @Insert
    suspend fun saveMovieDetails(movieDetailsResponse: MovieDetailsResponse)

    @Query("DELETE FROM movie_details WHERE imdbId =:imdbId")
    suspend fun deleteMovieDetails(imdbId: String)

    @Query("SELECT * FROM movie_details WHERE imdbId =:id")
    suspend fun getMovieDetailsByImdbIdFromDb(id: String): MovieDetailsResponse

}