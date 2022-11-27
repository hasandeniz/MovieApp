package com.example.movieapp.retrofit

class ApiUtils {
    companion object{
        var BASE_URL = "http://www.omdbapi.com/"

        fun getMovieDao() : MovieDao{
            return RetrofitClient.getClient(BASE_URL).create(MovieDao::class.java)
        }
    }
}