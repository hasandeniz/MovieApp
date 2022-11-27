package com.example.movieapp.data.repository

import com.example.movieapp.data.datasource.FavoritesDataSource
import com.example.movieapp.data.model.Movie

class FavoritesRepository(var favoritesDataSource: FavoritesDataSource) {

    suspend fun getAllFavoriteMovies(): List<Movie> = favoritesDataSource.getAllFavoriteMovies()

    suspend fun addMovieToFavorites(movie: Movie) = favoritesDataSource.addMovieToFavorites(movie)

    suspend fun removeMovieFromFavorites(id: Int) = favoritesDataSource.removeMovieFromFavorites(id)

    suspend fun removeAllMoviesFromFavorites() = favoritesDataSource.removeAllMoviesFromFavorites()

    suspend fun getFavoriteMovieByImdbId(id: String): Movie = favoritesDataSource.getFavoriteMovieByImdbId(id)
}