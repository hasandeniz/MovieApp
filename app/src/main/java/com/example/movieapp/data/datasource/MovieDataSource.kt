package com.example.movieapp.data.datasource

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.movieapp.data.model.MovieDetailsResponse
import com.example.movieapp.retrofit.MovieDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDataSource(var movieDao: MovieDao) {

    fun searchMovieByTitle(title: String) =
       Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(movieDao, title) }
        ).flow

    suspend fun getMovieByImdbId(imdbID: String): MovieDetailsResponse =
        withContext(Dispatchers.IO){
            try{
                movieDao.getMovieByImdbId(imdbID)
            }catch (exception: Exception){
                Log.e("MovieDataSource",exception.message,exception)
                MovieDetailsResponse(error = exception.message)
            }
        }
}