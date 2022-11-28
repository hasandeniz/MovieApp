package com.example.movieapp.ui.movielist

import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieListAdapter: MovieListAdapter
    private val movieListViewModel: MovieListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        binding.moviesViewModel = movieListViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieListViewModel.getAllFavorites()
        movieListAdapter = MovieListAdapter(MovieListAdapter.FavoritesListener { movie, button ->
            if (button.tag == "not favorite") {
                if (movieListViewModel.isOnline(requireContext())){
                    button.tag = "favorite"
                    (button as ImageButton).setImageResource(R.drawable.ic_favorite)
                    movieListViewModel.saveMovieToFavorites(movie)
                    movieListViewModel.saveMovieDetailsToFavorites(movie)
                    Snackbar.make(view,"${movie.title} Added to Favorites",Snackbar.LENGTH_SHORT).show()
                }else
                    Toast.makeText(context,"No internet connection!",Toast.LENGTH_SHORT).show()
            }else{
                button.tag = "not favorite"
                (button as ImageButton).setImageResource(R.drawable.ic_not_favorite)
                movieListViewModel.removeMovieFromFavorites(movie)
                movieListViewModel.removeMovieDetailsFromFavorites(movie)
                Snackbar.make(view,"${movie.title} Removed from Favorites",Snackbar.LENGTH_SHORT).show()
            }
        },movieListViewModel.favorites)

        binding.adapter = movieListAdapter

        adjustToolbarMenu(view)
        showErrorMessage()
        observeData()

    }

    private fun adjustToolbarMenu(view: View){
        val menuHost = requireActivity() as MenuHost
        val menuProvider: MenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.dummy, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.dummy_icon -> {
                        val action = MovieListFragmentDirections.actionMovieListFragmentToFavoritesFragment()
                        Navigation.findNavController(view).navigate(action)
                    }
                }
                return true
            }
        }
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showErrorMessage(){
        viewLifecycleOwner.lifecycleScope.launch {
            movieListAdapter.loadStateFlow.collectLatest { loadStates ->
                if(loadStates.refresh is LoadState.Error){
                    movieListAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
                    val errorMessage = (loadStates.refresh as LoadState.Error).error.message
                    Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieListViewModel.viewState.collectLatest { movieViewState ->
                    binding.isLoading = movieViewState.isLoading
                    movieViewState.movieResponse?.collectLatest {
                        movieListAdapter.submitData(viewLifecycleOwner.lifecycle,it)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        movieListViewModel.index = 1
    }

}