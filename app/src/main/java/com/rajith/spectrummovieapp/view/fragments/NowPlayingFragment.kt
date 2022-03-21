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
import com.rajith.spectrummovieapp.view.activities.MoviesActivity
import com.rajith.spectrummovieapp.view.adapters.MovieAdapter
import com.rajith.spectrummovieapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_now_playing.*

@AndroidEntryPoint
class NowPlayingFragment : Fragment(R.layout.fragment_now_playing) {

    lateinit var viewModel: MoviesViewModel
    lateinit var newsAdapter: MovieAdapter
    private val TAG = "NowPlayingFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesActivity).viewModel
        setupRecyclerView()

        viewModel.getNowPlayingMovies()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie_id", it.id)
            }
            findNavController().navigate(
                R.id.action_nowPlayingFragment_to_movieDetailFragment,
                bundle
            )
        }

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.results)
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
//        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = MovieAdapter()
        rvNowPlaying.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}