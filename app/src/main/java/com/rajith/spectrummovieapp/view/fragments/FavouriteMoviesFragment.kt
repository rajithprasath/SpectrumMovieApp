package com.rajith.spectrummovieapp.view.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.view.adapters.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favourite_movies.*


@AndroidEntryPoint
class FavouriteMoviesFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_favourite_movies

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun observeData() {
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


    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        rvFavouriteMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}