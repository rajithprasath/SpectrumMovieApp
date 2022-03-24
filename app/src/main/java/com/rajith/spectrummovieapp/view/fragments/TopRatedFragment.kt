package com.rajith.spectrummovieapp.view.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.core.util.Constants
import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.MovieMapper.fillGenre
import com.rajith.spectrummovieapp.view.activities.MoviesListActivity
import com.rajith.spectrummovieapp.view.adapters.MovieAdapter
import com.rajith.spectrummovieapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_now_playing.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_top_rated.*

@AndroidEntryPoint
class TopRatedFragment : Fragment(R.layout.fragment_top_rated) {

    lateinit var viewModel: MoviesViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesListActivity).viewModel
        setHasOptionsMenu(true)
        setupRecyclerView()
        observeData()

        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movieId", it.id)
            }
            findNavController().navigate(
                R.id.action_topRatedFragment_to_movieDetailFragment,
                bundle
            )
        }
    }

    private fun observeData() {
        viewModel.getTopRatedMovies()
        viewModel.topRatedMovies.observe(viewLifecycleOwner, Observer { response ->
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
                        val totalPages = movieResponse.total_results / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.topRatedPage == totalPages
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

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getTopRatedMovies()
                isScrolling = false
            } else {
                rvTopRated.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        rvTopRated.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@TopRatedFragment.scrollListener)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.movieSearchFragment -> {
                findNavController().navigate(
                    R.id.action_topRatedFragment_to_movieSearchFragment
                )
            }
            R.id.favouriteMoviesFragment -> {
                findNavController().navigate(
                    R.id.action_topRatedFragment_to_favouriteMoviesFragment
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }


}