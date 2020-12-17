package com.example.madlevel6task2.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madlevel6task2.R
import com.example.madlevel6task2.databinding.ItemMovieBinding
import com.example.madlevel6task2.model.MovieJsonReponse.Movie

class MovieAdapter (
    private val movies: List<Movie>,
    private val onClick: (Movie) -> Unit
): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val binding = ItemMovieBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                onClick(movies[adapterPosition])
            }
        }

        fun dataBind(movie: Movie) {
            binding.txtNumber.text = "%s.".format((movies.indexOf(movie)+1).toString())
            Glide.with(context).load(movie.getMovieImageUrl(movie.poster_path)).into(binding.ivCover)
        }
    }

    override fun onCreate(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false))
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(movies[position])
    }
}