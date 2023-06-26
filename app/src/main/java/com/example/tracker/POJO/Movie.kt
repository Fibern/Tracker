package com.example.tracker.POJO

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("Title") val Title: String,
    @SerializedName("Year") val Year: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Type") val Type: String? = null,
    @SerializedName("Poster") val Poster: String,
    @SerializedName("Rated") val Rated: String? = null,
    @SerializedName("Released") val Released: String? = null,
    @SerializedName("Runtime") val Runtime: String? = null,
    @SerializedName("Genre") val Genre: String? = null,
    @SerializedName("Director") val Director: String? = null,
    @SerializedName("Writer") val Writer: String? = null,
    @SerializedName("Actors") val Actors: String? = null,
    @SerializedName("Plot") val Plot: String? = null,
    @SerializedName("Language") val Language: String? = null,
    @SerializedName("Country") val Country: String? = null,
    @SerializedName("Awards") val Awards: String? = null,
    @SerializedName("Ratings") val Ratings: List<Rating>? = null,
    @SerializedName("Metascore") val Metascore: String? = null,
    @SerializedName("imdbRating") val imdbRating: String? = null,
    @SerializedName("imdbVote") val imdbVotes: String? = null,
    @SerializedName("DVD") val DVD: String? = null,
    @SerializedName("BoxOffice") val BoxOffice: String? = null,
    @SerializedName("Production") val Produciton: String? = null,
    @SerializedName("Website") val Website: String? = null,
    @SerializedName("Response") val Response: String? = null
    ){
    override fun toString(): String {
        return Title
    }
}
