package com.example.movieapp.data.datasource

import com.example.movieapp.data.model.Movie
import com.example.movieapp.room.FavoritesDao

class FavoritesDataSource(var favoritesDao: FavoritesDao) {

    suspend fun getAllFavoriteMovies(): List<Movie> = favoritesDao.getAllFavoriteMovies()

    suspend fun addMovieToFavorites(movie: Movie) = favoritesDao.addMovieToFavorites(movie)

    suspend fun removeMovieFromFavorites(id: Int) = favoritesDao.removeMovieFromFavorites(id)

    suspend fun removeAllMoviesFromFavorites() = favoritesDao.removeAllMoviesFromFavorites()

    suspend fun getFavoriteMovieByImdbId(id: String): Movie = favoritesDao.getFavoriteMovieByImdbId(id)
}