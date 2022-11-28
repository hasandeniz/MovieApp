package com.example.movieapp.data.datasource

import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.MovieDetailsResponse
import com.example.movieapp.room.FavoritesDao

class FavoritesDataSource(private var favoritesDao: FavoritesDao) {

    suspend fun getAllFavoriteMovies(): List<Movie> = favoritesDao.getAllFavoriteMovies()

    suspend fun addMovieToFavorites(movie: Movie) = favoritesDao.addMovieToFavorites(movie)

    suspend fun removeMovieFromFavorites(movie: Movie) = favoritesDao.removeMovieFromFavorites(movie)

    suspend fun saveMovieDetails(movieDetailsResponse: MovieDetailsResponse) = favoritesDao.saveMovieDetails(movieDetailsResponse)

    suspend fun deleteMovieDetails(imdbId: String) = favoritesDao.deleteMovieDetails(imdbId)

    suspend fun getMovieDetailsByImdbIdFromDb(imdbId: String): MovieDetailsResponse = favoritesDao.getMovieDetailsByImdbIdFromDb(imdbId)
}