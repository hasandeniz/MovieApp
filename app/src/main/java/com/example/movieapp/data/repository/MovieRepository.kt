package com.example.movieapp.data.repository

import androidx.paging.PagingData
import com.example.movieapp.data.datasource.MovieDataSource
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.MovieDetailsResponse
import kotlinx.coroutines.flow.Flow

class MovieRepository(var movieDataSource: MovieDataSource) {

    suspend fun getMovieByImdbId(imdbID: String): MovieDetailsResponse = movieDataSource.getMovieByImdbId(imdbID)

    fun searchMovieByTitle(title:String): Flow<PagingData<Movie>> = movieDataSource.searchMovieByTitle(title)

}