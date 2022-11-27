package com.example.movieapp.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.MovieDetailsResponse
import com.example.movieapp.data.repository.FavoritesRepository
import com.example.movieapp.data.repository.MovieRepository
import com.example.movieapp.error.ConsumableError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private var movieRepository: MovieRepository,
                                                private var favoritesRepository: FavoritesRepository ) : ViewModel(){

    private val _viewState = MutableStateFlow(MovieDetailsViewState())
    val viewState : StateFlow<MovieDetailsViewState> get() = _viewState.asStateFlow()

    private val movieDetailsLiveData = MutableLiveData<MovieDetailsResponse>()
    val isFavoriteLiveData = MutableLiveData<Boolean>()

    fun isFavorite(imdbId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val favoritesList = favoritesRepository.getAllFavoriteMovies()
            val groupedFavorites = favoritesList.groupBy(Movie::imdbId)
            isFavoriteLiveData.postValue(!groupedFavorites[imdbId].isNullOrEmpty())
        }
    }

    fun getMovieDetailsByImdbIdFromApi(imdbId: String){
        viewModelScope.launch {
            _viewState.update {
                it.copy(isLoading = true)
            }
            val movieDetails = movieRepository.getMovieDetailsByImdbIdFromApi(imdbId)
            if (movieDetails.response == "True"){
                movieDetailsLiveData.value = movieDetails
                _viewState.update {
                    it.copy(
                        isLoading = false,
                        movieDetailsResponse = movieDetailsLiveData
                    )
                }
            }else{
                addErrorToList(movieDetails.error)
            }

        }
    }

    fun getMovieDetailsByImdbIdFromDb(imdbId: String){
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.update {
                it.copy(isLoading = true)
            }
            movieDetailsLiveData.postValue(favoritesRepository.getMovieDetailsByImdbIdFromDb(imdbId))
            _viewState.update {
                it.copy(
                    isLoading = false,
                    movieDetailsResponse = movieDetailsLiveData
                )
            }
        }
    }

    private fun addErrorToList(exception: String?) {
        exception?.let {
            val errorList =
                _viewState.value.consumableErrors?.toMutableList() ?: mutableListOf()
            errorList.add(ConsumableError(exception = it))
            _viewState.value =
                viewState.value.copy(
                    consumableErrors = errorList,
                    isLoading = false
                )
        }
    }

    fun errorConsumed(errorId: Long) {
        _viewState.update { currentUiState ->
            val newConsumableError =
                currentUiState.consumableErrors?.filterNot { it.id == errorId }
            currentUiState.copy(consumableErrors = newConsumableError, isLoading = false)
        }
    }

    data class MovieDetailsViewState(
        val isLoading: Boolean? = false,
        val movieDetailsResponse: LiveData<MovieDetailsResponse>? = null,
        val consumableErrors: List<ConsumableError>? = null
    )
}