package com.example.tracker.Adapters

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import com.example.tracker.Fragments.MovieItemFragment
import com.example.tracker.Fragments.NavigationHost
import com.example.tracker.POJO.Movie
import com.example.tracker.R

class MovieCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    lateinit var movieImage: ImageView
    lateinit var movieTitle: TextView
    lateinit var movieDate: TextView
    lateinit var movie: Movie
    lateinit var view: View
    init{
        movieImage = itemView.findViewById(R.id.movie_image)
        movieTitle = itemView.findViewById(R.id.movie_title)
        movieDate = itemView.findViewById(R.id.movie_date)
        view = itemView
        view.setOnClickListener {
            val bundle = Bundle()
            val fragment = MovieItemFragment()
            bundle.putString("txt" , movie.Title)
            bundle.putString("id", movie.imdbID)
            fragment.arguments = bundle
            val activity = view.context
            (activity as NavigationHost).navigateTo(fragment, true)
        }
    }


}