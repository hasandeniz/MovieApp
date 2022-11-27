package com.example.movieapp.data.repository

import com.example.movieapp.data.datasource.FavoritesDataSource
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.MovieDetailsResponse

class FavoritesRepository(private var favoritesDataSource: FavoritesDataSource) {

    suspend fun getAllFavoriteMovies(): List<Movie> = favoritesDataSource.getAllFavoriteMovies()

    suspend fun addMovieToFavorites(movie: Movie) = favoritesDataSource.addMovieToFavorites(movie)

    suspend fun removeMovieFromFavorites(id: Int) = favoritesDataSource.removeMovieFromFavorites(id)

    suspend fun getFavoriteMovieByImdbId(id: String): Movie = favoritesDataSource.getFavoriteMovieByImdbId(id)

    suspend fun saveMovieDetails(movieDetailsResponse: MovieDetailsResponse) = favoritesDataSource.saveMovieDetails(movieDetailsResponse)

    suspend fun deleteMovieDetails(id: Int) = favoritesDataSource.deleteMovieDetails(id)

    suspend fun getMovieDetailsByImdbIdFromDb(imdbId: String): MovieDetailsResponse = favoritesDataSource.getMovieDetailsByImdbIdFromDb(imdbId)
}