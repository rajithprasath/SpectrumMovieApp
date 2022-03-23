package com.rajith.spectrummovieapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.core.util.Constants
import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.Movie
import com.rajith.spectrummovieapp.view.activities.MoviesListActivity
import com.rajith.spectrummovieapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_detail.*

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    lateinit var viewModel: MoviesViewModel
    private val args: MovieDetailFragmentArgs by navArgs()
    private val TAG = "MovieDetailFragment"
    lateinit var movie: Movie
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesListActivity).viewModel
        val movieId = args.movieId
        observeData(movieId)

        fab.setOnClickListener {
            movie.let {
                viewModel.saveMovie(movie)
                Snackbar.make(view, "Movie saved successfully", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeData(movieId: Int){
        viewModel.getMovieDetail(movieId)

        viewModel.movie.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movieResponse ->
                        showMovieDetails(movieResponse)
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

    private fun showMovieDetails(movie: Movie) {
        this.movie = movie
        Glide.with(this).load(Constants.IMAGE_BASE_URL + movie.backdrop_path).into(ivBackDrop)
        Glide.with(this).load(Constants.IMAGE_BASE_URL + movie.poster_path).into(ivPoster)
        tvTitle.text = movie.title
        tvReleaseDate.text = movie.release_date
        tvVoteCount.text = movie.vote_count.toString()
        tvOverview.text = movie.overview
        fab.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

}