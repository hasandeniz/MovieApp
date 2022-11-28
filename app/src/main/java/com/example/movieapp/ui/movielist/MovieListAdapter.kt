package com.example.movieapp.ui.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movieapp.R
import com.example.movieapp.data.model.Movie
import com.example.movieapp.databinding.MovieListItemBinding
import com.example.movieapp.databinding.SeparatorItemBinding

class MovieListAdapter(private val clickListener: FavoritesListener,
                       private val favoritesList: ArrayList<Movie>) : PagingDataAdapter<UiModel, ViewHolder>(MovieListUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == R.layout.movie_list_item){
            val binding = MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MoviesViewHolder(binding)
        }else{
            val binding = SeparatorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SeparatorViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)){
            is UiModel.MovieItem -> R.layout.movie_list_item
            is UiModel.SeparatorItem -> R.layout.separator_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            val uiModel = getItem(position)
            uiModel.let {
                when (uiModel){
                    is UiModel.MovieItem -> (holder as MoviesViewHolder).bind(uiModel, clickListener, favoritesList)
                    is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(uiModel)
                    else -> {}
                }
                if (holder is MoviesViewHolder && uiModel is UiModel.MovieItem){
                    val binding = holder.mbinding
                    binding.cvMovieListItem.setOnClickListener {
                        val action = MovieListFragmentDirections.actionNavigationMovieListToMovieDetailsFragment(uiModel.movie.imdbId, uiModel.movie.title,false)
                        Navigation.findNavController(it).navigate(action)
                    }

                    binding.ibMovieItemFav.setOnClickListener {
                        clickListener.onClick(uiModel.movie,it)
                    }
                }
            }
        }
    }

    class MoviesViewHolder(private val binding: MovieListItemBinding) : ViewHolder(binding.root) {
        var mbinding = binding

        fun bind(item: UiModel.MovieItem, clickListener: FavoritesListener, favoritesList: List<Movie>) {
            binding.clickListener = clickListener
            binding.movie = item.movie
            val groupedFavorites = favoritesList.groupBy(Movie::imdbId)
            if (groupedFavorites[item.movie.imdbId].isNullOrEmpty()){
                binding.ibMovieItemFav.tag = "not favorite"
                binding.ibMovieItemFav.setImageResource(R.drawable.ic_not_favorite)
            }
            else {
                binding.ibMovieItemFav.tag = "favorite"
                binding.ibMovieItemFav.setImageResource(R.drawable.ic_favorite)
            }
            binding.executePendingBindings()
        }
    }

    class SeparatorViewHolder(private val binding: SeparatorItemBinding) : ViewHolder(binding.root) {
        fun bind(item: UiModel.SeparatorItem){
            val separatorText = "Page " + item.pageNumber
            binding.tvPageNumber.text = separatorText
        }
    }

    class MovieListUtil : DiffUtil.ItemCallback<UiModel>() {
        override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
            return (oldItem is UiModel.MovieItem && newItem is UiModel.MovieItem && oldItem.movie.imdbId == newItem.movie.imdbId) ||
                    (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem && oldItem.pageNumber == newItem.pageNumber)
        }

        override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
            return (oldItem == newItem)
        }
    }

    class FavoritesListener(val clickListener: (movie: Movie, view: View) -> Unit){
        fun onClick(movie: Movie, view: View) = clickListener(movie,view)
    }
}
