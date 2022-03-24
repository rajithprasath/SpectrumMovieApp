package com.rajith.spectrummovieapp.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.view.activities.MoviesListActivity
import com.rajith.spectrummovieapp.view.adapters.MovieAdapter
import com.rajith.spectrummovieapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favourite_movies.*


@AndroidEntryPoint
class FavouriteMoviesFragment : Fragment(R.layout.fragment_favourite_movies) {

    lateinit var viewModel: MoviesViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesListActivity).viewModel
        setHasOptionsMenu(true)
        setupRecyclerView()
        observeData()

        tbFav.setNavigationOnClickListener { findNavController().navigateUp() }

        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movieId", it.id)
            }
            findNavController().navigate(
                R.id.action_favouriteMoviesFragment_to_movieDetailFragment,
                bundle
            )
        }
    }

    private fun observeData() {
        viewModel.getFavouriteMovies()
        viewModel.favoriteMovies.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movies ->
                        movieAdapter.differ.submitList(movies)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }


    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        rvFavouriteMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}