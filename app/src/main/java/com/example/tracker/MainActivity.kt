package com.example.tracker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.tracker.Fragments.LoginFragment
import com.example.tracker.Fragments.NavigationHost
import com.example.tracker.POJO.Movie
import com.example.tracker.POJO.Search
import com.example.tracker.Utilities.ApiUtilities
import com.example.tracker.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationHost {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        fetch()
        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, LoginFragment())
                .commit()
        }

    }
    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    private fun fetch(){
        ApiUtilities.getApiInterface()?.getMovie(i = "tt26160190")
            ?.enqueue(object : Callback<Movie>{
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                }

                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    val body = response.body() as Movie?
                }
            })
/*
        ApiUtilities.getApiInterface()?.getSearch(s = "star")
            ?.enqueue(object : Callback<Search>{
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<Search>, t: Throwable) {
                }

                override fun onResponse(call: Call<Search>, response: Response<Search>) {
                    val body = response.body() as Search
                }
            })
    */}
}