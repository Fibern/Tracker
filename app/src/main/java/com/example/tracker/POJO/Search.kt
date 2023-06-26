package com.example.tracker.POJO

import com.google.gson.annotations.SerializedName

data class Search(
    @SerializedName("Search") val Search: List<Movie>,
    @SerializedName("totalResults") val totalResult: String,
    @SerializedName("Response") val Response: String
)
