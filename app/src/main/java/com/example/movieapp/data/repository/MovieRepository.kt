package com.example.movieapp.data.repository

import androidx.paging.PagingData
import com.example.movieapp.data.datasource.MovieDataSource
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.MovieDetailsResponse
import kotlinx.coroutines.flow.Flow

class MovieRepository(private var movieDataSource: MovieDataSource) {

    suspend fun getMovieDetailsByImdbIdFromApi(imdbID: String): MovieDetailsResponse = movieDataSource.getMovieDetailsByImdbIdFromApi(imdbID)

    fun searchMovieByTitle(title:String): Flow<PagingData<Movie>> = movieDataSource.searchMovieByTitle(title)

}