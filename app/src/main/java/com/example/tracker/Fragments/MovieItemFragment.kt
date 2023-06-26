package com.example.tracker.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.tracker.POJO.Movie
import com.example.tracker.R
import com.example.tracker.Utilities.ApiUtilities
import com.example.tracker.databinding.FragmentMovieItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieItemFragment : Fragment() {


    private var _binding: FragmentMovieItemBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var id: String
    private lateinit var imdbId: String
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = FirebaseAuth.getInstance().currentUser!!.uid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieItemBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        arguments?.let{
            binding.toolbar.title = it.getString("txt")
            imdbId = it.getString("id")!!
            fetch()
            loadMovie()
        }
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
               requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.addButton.setOnClickListener {
            addMovie()
            binding.deleteButton.visibility = View.VISIBLE
            binding.addButton.visibility = View.GONE
        }

        binding.deleteButton.setOnClickListener{
            deleteMovie()
            binding.deleteButton.visibility = View.GONE
            binding.addButton.visibility = View.VISIBLE
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun String.addCharAtIndex(char: Char, index: Int) =
        StringBuilder(this).apply { insert(index, char) }.toString()

    private fun fetch(){
        ApiUtilities.getApiInterface()?.getMovie(i = imdbId)
            ?.enqueue(object : Callback<Movie>{
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                }

                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    val body = response.body()
                    movie = body!!
                    binding.date.text = movie.Year
                    binding.director.text = movie.Director
                    binding.writer.text = movie.Writer
                    binding.language.text = movie.Language
                    binding.country.text = movie.Country
                    if (movie.Poster != "N/A") {
                        Picasso.with(context)
                            .load(movie.Poster.addCharAtIndex('0', movie.Poster.length - 6))
                            .into(binding.movieImage, object : com.squareup.picasso.Callback {
                                override fun onSuccess() {
                                    binding.movieImage.scaleType = ImageView.ScaleType.CENTER_CROP
                                    binding.mainLayout.visibility = View.VISIBLE
                                    binding.sideLayout.visibility = View.GONE
                                }

                                override fun onError() {

                                }
                            })
                    }else{
                        binding.mainLayout.visibility = View.VISIBLE
                        binding.sideLayout.visibility = View.GONE
                    }
                }
            })
    }

    private fun addMovie(){

        val tmp = hashMapOf(
            "id" to imdbId,
            "Title" to movie.Title,
            "Poster" to movie.Poster,
            "Year" to movie.Year
        )

        db.collection("users").document(id)
            .collection("Movies").document(imdbId)
            .set(tmp)
    }

    private fun deleteMovie(){

        db.collection("users").document(id)
            .collection("Movies").document(imdbId)
            .delete()

    }

    private fun loadMovie(){
        db.collection("users").document(id)
            .collection("Movies").document(imdbId)
            .get()
            .addOnSuccessListener { result ->
                if(result["Title"] == null){
                    binding.addButton.visibility = View.VISIBLE
                    binding.deleteButton.visibility = View.GONE
                }else{
                    binding.addButton.visibility = View.GONE
                    binding.deleteButton.visibility = View.VISIBLE
                }
            }
    }

}