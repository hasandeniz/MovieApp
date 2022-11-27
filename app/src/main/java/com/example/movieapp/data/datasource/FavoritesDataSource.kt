package com.example.movieapp.data.datasource

import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.MovieDetailsResponse
import com.example.movieapp.room.FavoritesDao

class FavoritesDataSource(private var favoritesDao: FavoritesDao) {

    suspend fun getAllFavoriteMovies(): List<Movie> = favoritesDao.getAllFavoriteMovies()

    suspend fun addMovieToFavorites(movie: Movie) = favoritesDao.addMovieToFavorites(movie)

    suspend fun removeMovieFromFavorites(id: Int) = favoritesDao.removeMovieFromFavorites(id)

    suspend fun getFavoriteMovieByImdbId(id: String): Movie = favoritesDao.getFavoriteMovieByImdbId(id)

    suspend fun saveMovieDetails(movieDetailsResponse: MovieDetailsResponse) = favoritesDao.saveMovieDetails(movieDetailsResponse)

    suspend fun deleteMovieDetails(id: Int) = favoritesDao.deleteMovieDetails(id)

    suspend fun getMovieDetailsByImdbIdFromDb(imdbId: String): MovieDetailsResponse = favoritesDao.getMovieDetailsByImdbIdFromDb(imdbId)
}