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
import com.rajith.spectrummovieapp.domain.model.MovieMapper.fillGenre
import com.rajith.spectrummovieapp.view.activities.MoviesListActivity
import com.rajith.spectrummovieapp.view.adapters.MovieAdapter
import com.rajith.spectrummovieapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_now_playing.*

@AndroidEntryPoint
class NowPlayingFragment : Fragment(R.layout.fragment_now_playing) {

    lateinit var viewModel: MoviesViewModel
    private lateinit var movieAdapter: MovieAdapter
    private val TAG = "NowPlayingFragment"

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
                R.id.action_nowPlayingFragment_to_movieDetailFragment,
                bundle
            )
        }
    }

    private fun observeData() {
        viewModel.getNowPlayingMovies()
        viewModel.nowPlayingMovies.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movieResponse ->
                        movieAdapter.differ.submitList(movieResponse.results.map {
                            viewModel.genreList?.let { it1 ->
                                it.fillGenre(
                                    it1
                                )
                            }
                        })
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
        rvNowPlaying.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}