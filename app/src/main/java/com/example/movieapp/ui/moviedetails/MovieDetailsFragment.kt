package com.example.movieapp.ui.moviedetails

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()
    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieDetailsViewModel.isFavorite(args.imdbId)
        adjustFavoritesIcon()

        if (args.isFavorite)
            movieDetailsViewModel.getMovieDetailsByImdbIdFromDb(args.imdbId)
        else
            movieDetailsViewModel.getMovieDetailsByImdbIdFromApi(args.imdbId)

        observeData()


    }

    private fun adjustFavoritesIcon(){
        movieDetailsViewModel.isFavoriteLiveData.observe(viewLifecycleOwner){isFavorite->
            val menuHost: MenuHost = requireActivity()
            val menuProvider: MenuProvider = object : MenuProvider{
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menu.clear()
                    if (isFavorite)
                        menuInflater.inflate(R.menu.favorites_menu, menu)
                    else
                        menuInflater.inflate(R.menu.not_favorites_menu, menu)
                }
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return true
                }
            }
            menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }
    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieDetailsViewModel.viewState.collectLatest { movieViewState ->
                    movieViewState.consumableErrors?.let { consumableError ->
                        consumableError.firstOrNull()?.let { error ->
                            Toast.makeText(context, error.exception, Toast.LENGTH_SHORT).show()
                            movieDetailsViewModel.errorConsumed(error.id)
                        }
                    }

                    binding.isLoading = movieViewState.isLoading

                    movieViewState.movieDetailsResponse?.observe(viewLifecycleOwner){
                        Log.d("logger", it.toString())
                        binding.details = it
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}