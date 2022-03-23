package com.rajith.spectrummovieapp.view.fragments

import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_now_playing.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_upcoming.*

class UpcomingFragment : Fragment(R.layout.fragment_upcoming) {

    lateinit var viewModel: MoviesViewModel
    private lateinit var movieAdapter: MovieAdapter
    private val TAG = "UpcomingFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesListActivity).viewModel

        setupRecyclerView()
        observeData()

        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movieId", it.id)
            }
            findNavController().navigate(
                R.id.action_upcomingFragment_to_movieDetailFragment,
                bundle
            )
        }
    }

    private fun observeData() {
        viewModel.getUpcomingMovies()
        viewModel.upcomingMovies.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
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
        rvUpcoming.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}