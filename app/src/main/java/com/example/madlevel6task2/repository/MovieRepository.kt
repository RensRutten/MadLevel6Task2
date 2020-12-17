package com.example.madlevel6task2.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel6task2.api.MovieApi
import com.example.madlevel6task2.api.GetMovies
import com.example.madlevel6task2.model.MovieJsonReponse.Movie
import kotlinx.coroutines.withTimeout

class MovieError(message: String, cause: Throwable) : Throwable(message, cause)

class MovieRepository {

    private val getMovies: GetMovies = MovieApi.createApi()
    private val _movies: MutableLiveData<List<Movie>> = MutableLiveData()

    val movies: LiveData<List<Movie>> get() = _movies

    suspend fun getMovies(year: String?) {
        try {
            val parsedMovieResponse  = withTimeout(5_000) {
                getMovies.getMovies(year)
            }

            _movies.value = parsedMovieResponse.results

        } catch (error: Throwable) {
            throw MovieError("Unable to get movies", error)
        }
    }
}