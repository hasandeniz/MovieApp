package com.example.movieapp.data.repository

import com.example.movieapp.data.datasource.FavoritesDataSource
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.MovieDetailsResponse

class FavoritesRepository(private var favoritesDataSource: FavoritesDataSource) {

    suspend fun getAllFavoriteMovies(): List<Movie> = favoritesDataSource.getAllFavoriteMovies()

    suspend fun addMovieToFavorites(movie: Movie) = favoritesDataSource.addMovieToFavorites(movie)

    suspend fun removeMovieFromFavorites(movie: Movie) = favoritesDataSource.removeMovieFromFavorites(movie)

    suspend fun saveMovieDetails(movieDetailsResponse: MovieDetailsResponse) = favoritesDataSource.saveMovieDetails(movieDetailsResponse)

    suspend fun deleteMovieDetails(imdbId: String) = favoritesDataSource.deleteMovieDetails(imdbId)

    suspend fun getMovieDetailsByImdbIdFromDb(imdbId: String): MovieDetailsResponse = favoritesDataSource.getMovieDetailsByImdbIdFromDb(imdbId)
}