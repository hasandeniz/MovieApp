package com.example.movieapp.retrofit

import com.example.movieapp.data.model.MovieDetailsResponse
import com.example.movieapp.data.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDao {

    @GET("?apikey=37ad3207")
    suspend fun searchMovieByTitle(@Query("s") Title: String, @Query("page") Page: Int): MoviesResponse

    @GET("?apikey=37ad3207")
    suspend fun getMovieDetailsByImdbIdFromApi(@Query("i") imdbId: String): MovieDetailsResponse

}