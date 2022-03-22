package com.rajith.spectrummovieapp.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movies_list.*
import android.view.Menu
import android.view.MenuItem

@AndroidEntryPoint
class MoviesListActivity : AppCompatActivity() {

    val viewModel: MoviesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_list)

        bottomNavigationView.setupWithNavController(moviesNavHostFragment.findNavController())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.MovieSearchActivity -> startActivity(Intent(this,MovieSearchActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}