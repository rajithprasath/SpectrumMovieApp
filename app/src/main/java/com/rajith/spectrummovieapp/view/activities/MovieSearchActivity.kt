package com.rajith.spectrummovieapp.view.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.core.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.view.adapters.MovieAdapter
import com.rajith.spectrummovieapp.viewmodel.MovieSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movie_search.*
import kotlinx.android.synthetic.main.fragment_now_playing.*
import kotlinx.android.synthetic.main.fragment_now_playing.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieSearchActivity : AppCompatActivity() {

    val viewModel: MovieSearchViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private val TAG = "MovieSearchActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_search)
        setupRecyclerView()

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.searchMovies(editable.toString())
                    }
                }
            }
        }

        observeData()

    }

    private fun observeData(){
        viewModel.searchMovies.observe(this, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movieResponse ->
                        movieAdapter.differ.submitList(movieResponse.results)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        rvMovieSearch.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(this@MovieSearchActivity)
        }
    }
}