package com.example.movieapp.retrofit

import com.example.movieapp.BuildConfig
import com.example.movieapp.data.model.MovieDetailsResponse
import com.example.movieapp.data.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDao {

    @GET("?apikey=${BuildConfig.API_KEY}")
    suspend fun searchMovieByTitle(@Query("s") Title: String, @Query("page") Page: Int): MoviesResponse

    @GET("?apikey=${BuildConfig.API_KEY}")
    suspend fun getMovieByImdbId(@Query("i") imdbId: String): MovieDetailsResponse

}