package com.example.tracker.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.*
import com.example.tracker.Adapters.MovieCardRecycleViewAdapter
import com.example.tracker.Adapters.MovieGridItemDecoration
import com.example.tracker.POJO.Movie
import com.example.tracker.POJO.Rating
import com.example.tracker.POJO.Search
import com.example.tracker.Utilities.ApiUtilities
import com.example.tracker.databinding.FragmentMovieBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieFragment() : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var id: String
    private var list: ArrayList<Movie> = ArrayList()
    private var listSearched: ArrayList<Movie> = ArrayList()
    private var ogList: ArrayList<Movie> = ArrayList()
    private lateinit var  adapter: MovieCardRecycleViewAdapter
    private lateinit var arrayAdapter: ArrayAdapter<Movie>
    private var oldText: String = ""

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        lookForUser()
        listSearched.clear()
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        id = FirebaseAuth.getInstance().currentUser!!.uid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.appBar)
        adapter = MovieCardRecycleViewAdapter(list)
        binding.recyclerView.adapter = adapter

        val largePadding = resources.getDimensionPixelSize(R.dimen.shr_product_grid_spacing_mid)
        val smallPadding = resources.getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small)
        binding.recyclerView.addItemDecoration(MovieGridItemDecoration(largePadding, smallPadding))
        arrayAdapter = ArrayAdapter<Movie>(requireContext(), android.R.layout.simple_list_item_1, listSearched)
        binding.listView.adapter = arrayAdapter
        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val bundle = Bundle()
            val fragment = MovieItemFragment()
            val movie: Movie = parent.adapter.getItem(position) as Movie
            bundle.putString("txt" , movie.Title)
            bundle.putString("id", movie.imdbID)
            fragment.arguments = bundle
            val activity = view.context
            (activity as NavigationHost).navigateTo(fragment, true)
            arrayAdapter.clear()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)

        val menuItem: MenuItem = menu.findItem(R.id.search)
        val searchView = menu.findItem(R.id.search).actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Type here to search"
        searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text).setHintTextColor(Color.LTGRAY)
        searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text).setTextColor(Color.WHITE)

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (query.isNotEmpty()) {
                        adapter.updateData(listSearched)
                        binding.scrollView.visibility = View.VISIBLE
                        binding.listView.visibility = View.GONE
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null) {
                    if (newText.isNotEmpty()) fetchSearched(newText)
                    if (oldText != newText && newText.isNotEmpty()) {
                           binding.scrollView.visibility = View.GONE
                           binding.listView.visibility = View.VISIBLE
                    }
                    oldText = newText
                }
                return false
            }
        })

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                binding.scrollView.visibility = View.GONE
                binding.listView.visibility = View.VISIBLE
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                listSearched.clear()
                adapter.updateData(ogList)
                binding.scrollView.visibility = View.VISIBLE
                binding.listView.visibility = View.GONE
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun loadMovieList(){
        list.clear()
        ogList.clear()
        db.collection("users").document(id)
            .collection("Movies")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val p = document["Poster"].toString()
                    val t = document["Title"].toString()
                    val y = document["Year"].toString()
                    val i = document["id"].toString()
                    val movie = Movie(Poster = p, Title = t, Year = y, imdbID = i)
                    list.add(movie)
                    ogList.add(movie)
                    binding.recyclerView.adapter!!.notifyDataSetChanged()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun lookForUser(){
        val tmp = hashMapOf(
            "id" to id
        )
        db.collection("users")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty){
                    db.collection("users").document(id)
                        .set(tmp)
                }else{
                    loadMovieList()
                }
            }
    }

    private fun fetchSearched(s: String, page: Int = 1){
        listSearched.clear()
        (binding.listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        ApiUtilities.getApiInterface()?.getSearch(s = s, page = page)
            ?.enqueue(object : Callback<Search> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<Search>, t: Throwable) {
                }

                override fun onResponse(call: Call<Search>, response: Response<Search>) {
                    val body = response.body() as Search
                    if (body.Search != null) {
                        for (item in body.Search) {
                            listSearched.add(item)
                            (binding.listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                        }
                    }
                }
            })
    }

}