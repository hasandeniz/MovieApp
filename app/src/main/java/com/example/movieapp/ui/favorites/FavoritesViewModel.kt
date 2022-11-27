package com.example.movieapp.ui.favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private var favoritesRepository: FavoritesRepository) : ViewModel(){

    private var _favoritesLiveData = MutableLiveData<List<Movie>>()
    val favoritesLiveData : LiveData<List<Movie>> get() = _favoritesLiveData

    init {
        getFavorites()
    }

    private fun getFavorites(){
        viewModelScope.launch(Dispatchers.IO) {
            _favoritesLiveData.postValue(favoritesRepository.getAllFavoriteMovies())
        }
    }

    fun removeMovieFromFavorites(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieId = favoritesRepository.getFavoriteMovieByImdbId(movie.imdbId).id
            favoritesRepository.removeMovieFromFavorites(movieId)
            getFavorites()
            Log.d("MovieListViewModel", "${movie.title} removed from database")
        }
    }

    fun removeMovieDetailsFromFavorites(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = favoritesRepository.getMovieDetailsByImdbIdFromDb(movie.imdbId).id
            favoritesRepository.deleteMovieDetails(id)
            Log.d("MovieListViewModel", "${movie.title} details removed from database")
        }
    }

}