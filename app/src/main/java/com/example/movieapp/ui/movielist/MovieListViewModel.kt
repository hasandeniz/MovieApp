package com.example.movieapp.ui.movielist

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.repository.FavoritesRepository
import com.example.movieapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private var movieRepository: MovieRepository,
                                             private var favoritesRepository: FavoritesRepository) : ViewModel() {

    private val _viewState = MutableStateFlow(MovieListViewState())
    val viewState: StateFlow<MovieListViewState> = _viewState.asStateFlow()

    val favorites = arrayListOf<Movie>()
    var index = 1

    init {
        getAllFavorites()
    }

    private fun getMovie(title: String): Flow<PagingData<UiModel>> =
        movieRepository.searchMovieByTitle(title)
            .map { pagingData -> pagingData.map { UiModel.MovieItem(it,index++) } }
            .map {
                it.insertSeparators { before: UiModel.MovieItem?, after: UiModel.MovieItem? ->
                    //end of the list
                    if (after == null)
                        return@insertSeparators null

                    //beginning of the list
                    if (before == null)
                        return@insertSeparators UiModel.SeparatorItem(pageNumber = 1)

                    //checking every 10th item
                    if (before.index.rem(10) == 0)
                        return@insertSeparators UiModel.SeparatorItem(pageNumber = (before.index/10) + 1)
                    else{
                        null
                    }
                }
            }

    fun fetchPopularMovies(title: String) {
        index = 1
        viewModelScope.launch {
            _viewState.update {
                it.copy(
                    isLoading = true
                )
            }
            val movies = getMovie(title)

            _viewState.update {
                it.copy(
                    isLoading = false,
                    movieResponse = movies
                )
            }
        }
    }

    fun getAllFavorites(){
        viewModelScope.launch(Dispatchers.IO) {
            favorites.clear()
            favorites.addAll(favoritesRepository.getAllFavoriteMovies())
        }
    }

    fun saveMovieDetailsToFavorites(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieDetails = movieRepository.getMovieDetailsByImdbIdFromApi(movie.imdbId)
            favoritesRepository.saveMovieDetails(movieDetails)
            Log.d("MovieListViewModel", "${movieDetails.title} details saved to database")
        }
    }

    fun removeMovieDetailsFromFavorites(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesRepository.deleteMovieDetails(movie.imdbId)
            Log.d("MovieListViewModel", "${movie.title} details removed from database")
        }
    }

    fun saveMovieToFavorites(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesRepository.addMovieToFavorites(movie)
            getAllFavorites()
            Log.d("MovieListViewModel", "${movie.title} saved to database")
        }
    }

    fun removeMovieFromFavorites(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesRepository.removeMovieFromFavorites(movie)
            getAllFavorites()
            Log.d("MovieListViewModel", "${movie.title} removed from database")
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
            return false
        }else{
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
            return false
        }

    }

}

data class MovieListViewState(
    val isLoading: Boolean? = false,
    val movieResponse: Flow<PagingData<UiModel>>? = null,
)

sealed class UiModel{
    data class MovieItem(val movie: Movie, val index: Int) : UiModel()
    data class SeparatorItem(val pageNumber: Int) : UiModel()
}