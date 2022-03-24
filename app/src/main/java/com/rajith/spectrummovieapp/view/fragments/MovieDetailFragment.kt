package com.rajith.spectrummovieapp.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.rajith.spectrummovieapp.data.util.DateTimeFormatter.format

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    lateinit var viewModel: MoviesViewModel
    private val args: MovieDetailFragmentArgs by navArgs()
    lateinit var movie: Movie
    var isMovieExistInDb = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesListActivity).viewModel
        val movieId = args.movieId
        checkMovieExist(movieId)
        observeData(movieId)

        ibFav.setOnClickListener {
            if (isMovieExistInDb) {
                Snackbar.make(view, getString(R.string.already_saved_text), Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                movie.let {
                    viewModel.saveMovie(movie)
                    Snackbar.make(view, getString(R.string.saved_text), Snackbar.LENGTH_SHORT)
                        .show()
                    ibFav.setImageResource(R.drawable.ic_favorite)
                }
            }

        }

        detailToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun checkMovieExist(movieId: Int) {
        MainScope().launch(Dispatchers.IO) {
            if (viewModel.isMoveExist(movieId)) {
                isMovieExistInDb = true
                withContext(Dispatchers.Main) {
                    ibFav.setImageResource(R.drawable.ic_favorite)
                }
            }
        }
    }

    private fun observeData(movieId: Int) {
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
                        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
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
        tbTitle.title = movie.title
        tvOverview.text = movie.overview
        tvTagLine.text = movie.tagline
        tvStatus.text = movie.status
        tvReleaseDate.text = format(movie.release_date)
        val genresText = movie.genres?.joinToString { it -> "${it.name}" }
        val languageText = movie.spoken_languages?.joinToString { it -> "${it.english_name}" }
        tvGenres.text = genresText
        tvLanguage.text = languageText
        tvVoteAverage.text = movie.vote_average?.toString()
        tvVoteCount.text = movie.vote_count?.toString()
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

}