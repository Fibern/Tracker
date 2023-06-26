package com.example.tracker.Utilities

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {

    private var retrofit: Retrofit? = null
    var BASE_URL = "https://www.omdbapi.com"
    fun getApiInterface():ApiInterface?{
        if (retrofit == null){
            retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit!!.create(ApiInterface::class.java)
    }
}