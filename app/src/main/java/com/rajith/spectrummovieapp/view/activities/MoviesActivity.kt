package com.rajith.spectrummovieapp.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movies.*

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

    val viewModel: MoviesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        bottomNavigationView.setupWithNavController(moviesNavHostFragment.findNavController())
    }
}