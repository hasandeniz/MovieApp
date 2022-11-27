package com.example.movieapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.MovieDetailsResponse

@Dao
interface FavoritesDao {

    @Insert
    suspend fun addMovieToFavorites(movie: Movie)

    @Query("SELECT * FROM movies WHERE imdbID = :id")
    suspend fun getFavoriteMovieByImdbId(id: String): Movie

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun removeMovieFromFavorites(id: Int)

    @Query("SELECT * FROM movies")
    suspend fun getAllFavoriteMovies() : List<Movie>

    @Insert
    suspend fun saveMovieDetails(movieDetailsResponse: MovieDetailsResponse)

    @Query("DELETE FROM movie_details WHERE id =:id")
    suspend fun deleteMovieDetails(id: Int)

    @Query("SELECT * FROM movie_details WHERE imdbId =:id")
    suspend fun getMovieDetailsByImdbIdFromDb(id: String): MovieDetailsResponse

}