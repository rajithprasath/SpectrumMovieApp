package com.rajith.spectrummovieapp.view.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.core.util.Constants
import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.MovieMapper.fillGenre
import com.rajith.spectrummovieapp.view.adapters.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_now_playing.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_upcoming.*

@AndroidEntryPoint
class UpcomingFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_upcoming

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun observeData() {
        viewModel.getUpcomingMovies()
        viewModel.upcomingMovies.observe(viewLifecycleOwner, Observer { response ->
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
                        isLastPage = viewModel.upcomingPage == totalPages
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
                viewModel.getUpcomingMovies()
                isScrolling = false
            } else {
                rvUpcoming.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    override fun hideProgressBar() {
        paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    override fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        rvUpcoming.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@UpcomingFragment.scrollListener)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.movieSearchFragment -> {
                findNavController().navigate(
                    R.id.action_upcomingFragment_to_movieSearchFragment
                )
            }
            R.id.favouriteMoviesFragment -> {
                findNavController().navigate(
                    R.id.action_upcomingFragment_to_favouriteMoviesFragment
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }


}