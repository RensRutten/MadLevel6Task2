package com.example.madlevel6task2.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel6task2.model.MovieJsonReponse.Movie
import com.example.madlevel6task2.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel(application: Application): AndroidViewModel(application) {

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val movieRepository: MovieRepository = MovieRepository()

    private val _errorText: MutableLiveData<String> = MutableLiveData()

    val movies: LiveData<List<Movie>> = movieRepository.movies
    val errorText: LiveData<String> get() = _errorText

    fun getMovies(year: String?) {
        mainScope.launch {
            try {
                movieRepository.getMovies(year)
            } catch(error: Throwable) {
                _errorText.value = error.message
                Log.e("Movie error", error.cause.toString())
            }
        }
    }
}