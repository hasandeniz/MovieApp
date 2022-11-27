package com.example.movieapp.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.data.model.Movie
import com.example.movieapp.databinding.MovieListItemBinding
import com.example.movieapp.ui.movielist.MovieListAdapter

class FavoritesAdapter(private val clickListener: MovieListAdapter.FavoritesListener)
    : ListAdapter<Movie, FavoritesAdapter.FavoritesViewHolder>(FavoritesListUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        getItem(position)?.let {
            val item = getItem(position)
            val binding = holder.mbinding
            binding.cvMovieListItem.setOnClickListener { view ->
                val action = FavoritesFragmentDirections.actionFavoritesFragmentToMovieDetailsFragment(item.imdbId, item.title,true)
                Navigation.findNavController(view).navigate(action)
            }
            holder.bind(it,clickListener)
        }
    }

    class FavoritesViewHolder(private val binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var mbinding = binding
        fun bind(item: Movie, clickListener: MovieListAdapter.FavoritesListener) {
            binding.movie = item
            binding.clickListener = clickListener
            binding.ibMovieItemFav.setImageResource(R.drawable.ic_favorite)
            binding.executePendingBindings()
        }
    }

    class FavoritesListUtil : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.title == newItem.title
                    && oldItem.posterPath == newItem.posterPath
                    && oldItem.year == newItem.year
        }

    }
}
