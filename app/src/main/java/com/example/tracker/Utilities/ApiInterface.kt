package com.example.tracker.Utilities

import com.example.tracker.POJO.Movie
import com.example.tracker.POJO.Search
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("./")
    fun getSearch(
        @Query("apikey") apikey: String = Api.ApiKey,
        @Query("s") s: String,
        @Query("type") type: String = "movie",
        @Query("page") page: Int = 1
    ):Call<Search>

    @GET("./")
    fun getMovie(
        @Query("apikey") apikey: String = Api.ApiKey,
        @Query("i") i: String
    ):Call<Movie>

}