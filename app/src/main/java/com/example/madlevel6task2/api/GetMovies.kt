package com.example.madlevel6task2.api

import com.example.madlevel6task2.model.Movies
import retrofit2.http.GET
import retrofit2.http.Query

interface GetMovies {

    @GET("/3/discover/movie")
    suspend fun getMovies(@Query("year") year: String? = ""): Movies.Root


}