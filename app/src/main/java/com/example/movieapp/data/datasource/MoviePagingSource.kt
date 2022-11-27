package com.example.movieapp.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.data.model.Movie
import com.example.movieapp.retrofit.MovieDao
import retrofit2.HttpException
import java.io.IOException

private const val FIRST_PAGE_INDEX = 1

class MoviePagingSource(private val movieDao: MovieDao, private val title: String): PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: FIRST_PAGE_INDEX
        return try {
            val response = movieDao.searchMovieByTitle(title,position)
            val movies = response.movieList
            if (response.response == "False"){
                val exception = Exception(response.error)
                throw exception
            }

            LoadResult.Page(
                data = movies,
                prevKey = if (position == FIRST_PAGE_INDEX) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        }catch (exception: IOException){
            LoadResult.Error(exception)
        }catch (exception: HttpException){
            LoadResult.Error(exception)
        }catch (exception: Exception){
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}