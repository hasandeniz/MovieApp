package com.example.movieapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentFavoritesBinding
import com.example.movieapp.ui.movielist.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoritesAdapter: FavoritesAdapter
    private val favoritesViewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesAdapter = FavoritesAdapter(MovieListAdapter.FavoritesListener { movie, button ->
            (button as ImageButton).setImageResource(R.drawable.ic_not_favorite)
            favoritesViewModel.removeMovieFromFavorites(movie)
            favoritesViewModel.removeMovieDetailsFromFavorites(movie)
            Toast.makeText(context,"${movie.title} Removed from Favorites", Toast.LENGTH_SHORT).show()

        })
        binding.adapter = favoritesAdapter

        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch{
            favoritesViewModel.favoritesLiveData.observe(viewLifecycleOwner){ favoritesList->
                favoritesAdapter.submitList(favoritesList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}