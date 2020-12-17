package com.example.madlevel6task2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.madlevel6task2.model.Movies
import com.example.madlevel6task2.ui.MovieAdapter
import com.example.madlevel6task2.vm.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_get_movies.*
import kotlin.math.floor

const val BUNDLE_MOVIE_KEY = "bundle_movie_key"
const val REQ_MOVIE_KEY = "req_movie_key"

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SearchFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()

    private val movies = arrayListOf<Movies.Movie>()
    private val moviesAdapter = MovieAdapter(movies, ::onMovieClick)


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()

        btnSubmit.setOnClickListener {
            val year = txtYear.editText!!.text
            closeKeyboard()

            if(year.isEmpty()) {
                Snackbar.make(btnSubmit, R.string.year_validation_failed, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("SEARCH", "Getting movies for year: %s".format(year))

            progressbar.visibility = View.VISIBLE
            movieViewModel.getMovies(year.toString())
        }

        movieViewModel.movies.observe(viewLifecycleOwner, Observer {
            movies.clear()
            movies.addAll(it)
            moviesAdapter.notifyDataSetChanged()
            progressbar.visibility = View.INVISIBLE
            rvMovies.scrollToPosition(0)
        })

        movieViewModel.errorText.observe(viewLifecycleOwner, Observer {
            Snackbar.make(btnSubmit, R.string.failed_fetching_movies, Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun initRv() {
        val gridLayoutManager =  GridLayoutManager(context, 2)
        rvMovies.apply {
            adapter = moviesAdapter
            layoutManager = gridLayoutManager
        }

        rvMovies.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                rvMovies.viewTreeObserver.removeOnGlobalLayoutListener(this)
                gridLayoutManager.spanCount = calculateSpanCount()
                gridLayoutManager.requestLayout()
            }
        })

    }


    private fun onMovieClick(movie: Movies.Movie) {
        setFragmentResult(REQ_MOVIE_KEY, bundleOf(Pair(BUNDLE_MOVIE_KEY, movie)))
        findNavController().navigate(R.id.action_SearchFragment_to_InfoFragment)
    }

    private fun calculateSpanCount(): Int {
        val viewWidth = rvMovies.measuredWidth
        val cardViewWidth = resources.getDimension(R.dimen.poster_width)
        val cardViewMargin = resources.getDimension(R.dimen.margin_medium)
        val spanCount = floor((viewWidth / (cardViewWidth + cardViewMargin)).toDouble()).toInt()
        return if (spanCount >= 1) spanCount else 1
    }
    
    //Close keyboard
    private fun closeKeyboard() {
        // Null check to prevent from crashing
        if(activity?.currentFocus != null) {
            // Get input manager
            val inputManager: InputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            // Tell input manager to hide input (close)
            inputManager.hideSoftInputFromWindow(
                activity?.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}