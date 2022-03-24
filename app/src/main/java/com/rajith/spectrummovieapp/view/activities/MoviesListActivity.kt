package com.rajith.spectrummovieapp.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.view.fragments.NowPlayingFragmentDirections
import com.rajith.spectrummovieapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movies_list.*

@AndroidEntryPoint
class MoviesListActivity : AppCompatActivity() {

    val viewModel: MoviesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_list)

        val toolbar = toolbar
        setSupportActionBar(toolbar)
        onDestinationChanged(toolbar, bottomNavigationView)
        viewModel.getAllGenres()
        viewModel.genres.observe(this, Observer { response ->
            when (response) {
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


    private fun onDestinationChanged(toolbar: Toolbar, navView: BottomNavigationView) {
        moviesNavHostFragment.findNavController()
            .addOnDestinationChangedListener { _, navDestination, _ ->
                when (navDestination.id) {
                    R.id.movieSearchFragment -> {
                        toolbar.visibility = View.GONE
                        navView.visibility = View.GONE
                    }
                    R.id.movieDetailFragment -> {
                        toolbar.visibility = View.GONE
                        navView.visibility = View.GONE
                    }
                    R.id.favouriteMoviesFragment -> {
                        toolbar.visibility = View.GONE
                        navView.visibility = View.GONE
                    }
                    else -> {
                        toolbar.visibility = View.VISIBLE
                        navView.visibility = View.VISIBLE
                    }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }
}