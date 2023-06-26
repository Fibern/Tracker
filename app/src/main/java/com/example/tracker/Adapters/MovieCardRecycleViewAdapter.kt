package com.example.tracker.Adapters

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.POJO.Movie
import com.example.tracker.R
import com.squareup.picasso.Picasso

class MovieCardRecycleViewAdapter internal constructor(private var movieList: ArrayList<Movie>) : RecyclerView.Adapter<MovieCardViewHolder>(){

    lateinit var context: Context


    fun updateData(list: List<Movie>){
        movieList.clear()
        movieList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.grid_card, parent, false)
        context = parent.context
        return MovieCardViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: MovieCardViewHolder, position: Int) {
        if (position < movieList.size){
            val movie = movieList[position]
            holder.movie = movie
            holder.movieTitle.text = movie.Title
            holder.movieDate.text = movie.Year
            if (movie.Poster != "N/A") {
                Picasso.with(context).load(movie.Poster).into(holder.movieImage)
                holder.movieImage.scaleType = ImageView.ScaleType.CENTER_CROP
            }else {
                holder.movieImage.setImageResource(R.drawable.ic_baseline_broken_image_108)
                holder.movieImage.scaleType = ImageView.ScaleType.CENTER
            }
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

}