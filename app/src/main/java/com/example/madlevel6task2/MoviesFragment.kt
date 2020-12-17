package com.example.madlevel6task2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.example.madlevel6task2.model.Movies
import kotlinx.android.synthetic.main.fragment_movies.*


class MoviesFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFragmentResult()
    }

    private fun observeFragmentResult() {
        setFragmentResultListener(REQ_MOVIE_KEY) { _, bundle ->
            bundle.getParcelable<Movies.Movie>(BUNDLE_MOVIE_KEY)?.let { setUi(it) }
        }
    }

    private fun setUi(movie: Movies.Movie) {
        Log.d("UI", "setting ui")
        txtTitle.text = movie.title

        // Set context in oncreate?
        context?.let { Glide.with(it).load(movie.getMovieImageUrl(movie.backdrop_path)).into(ivInfoBanner) }
        context?.let { Glide.with(it).load(movie.getMovieImageUrl(movie.poster_path)).into(ivInfoCover) }

        txtRelease.text = movie.release_date
        txtOverview.text = movie.overview
        txtRating.text = movie.vote_average.toString()
    }
}