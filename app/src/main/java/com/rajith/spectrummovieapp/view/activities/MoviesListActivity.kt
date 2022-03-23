package com.rajith.spectrummovieapp.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movies_list.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.rajith.spectrummovieapp.core.util.Resource
import kotlinx.android.synthetic.main.activity_favourite_movies.*
import kotlinx.android.synthetic.main.activity_movies_list.progressBar

@AndroidEntryPoint
class MoviesListActivity : AppCompatActivity() {

    val viewModel: MoviesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_list)

        viewModel.getAllGenres()
        viewModel.genres.observe(this, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        bottomNavigationView.setupWithNavController(moviesNavHostFragment.findNavController())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.MovieSearchActivity -> startActivity(Intent(this,MovieSearchActivity::class.java))
            R.id.FavouriteMoviesActivity -> startActivity(Intent(this,FavouriteMoviesActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }
}