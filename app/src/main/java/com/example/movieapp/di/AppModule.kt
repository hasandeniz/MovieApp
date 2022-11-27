package com.example.movieapp.di

import android.content.Context
import com.example.movieapp.data.datasource.FavoritesDataSource
import com.example.movieapp.data.datasource.MovieDataSource
import com.example.movieapp.data.repository.FavoritesRepository
import com.example.movieapp.data.repository.MovieRepository
import com.example.movieapp.retrofit.ApiUtils
import com.example.movieapp.retrofit.MovieDao
import com.example.movieapp.room.FavoritesDao
import com.example.movieapp.room.FavoritesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMovieRepository(movieDataSource: MovieDataSource): MovieRepository{
        return MovieRepository(movieDataSource)
    }

    @Provides
    @Singleton
    fun provideMovieDataSource(movieDao: MovieDao): MovieDataSource{
        return MovieDataSource(movieDao)
    }

    @Provides
    @Singleton
    fun provideMovieDao(): MovieDao{
        return ApiUtils.getMovieDao()
    }

    @Provides
    @Singleton
    fun provideFavoritesRepository(favoritesDataSource: FavoritesDataSource): FavoritesRepository{
        return FavoritesRepository(favoritesDataSource)
    }

    @Provides
    @Singleton
    fun provideFavoritesDataSource(favoritesDao: FavoritesDao): FavoritesDataSource{
        return FavoritesDataSource(favoritesDao)
    }


    @Provides
    @Singleton
    fun provideFavoritesDao(favoritesDatabase: FavoritesDatabase): FavoritesDao{
        return favoritesDatabase.movieDatabaseDao()
    }

    @Provides
    @Singleton
    fun provideFavoritesDatabase(context: Context): FavoritesDatabase{
        return FavoritesDatabase.getMovieDatabase(context)
    }

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

}