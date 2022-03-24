package com.rajith.spectrummovieapp.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rajith.spectrummovieapp.MovieApplication
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.core.util.Constants
import com.rajith.spectrummovieapp.core.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.rajith.spectrummovieapp.core.util.Resource
import com.rajith.spectrummovieapp.domain.model.MovieMapper.fillGenre
import com.rajith.spectrummovieapp.view.adapters.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movies_list.*
import kotlinx.android.synthetic.main.fragment_movie_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieSearchFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_movie_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchMovies(editable.toString())
                    }
                }
            }
        }

        tbSearch.setNavigationOnClickListener { findNavController().navigateUp() }

        observeData()

        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movieId", it.id)
            }
            findNavController().navigate(
                R.id.action_movieSearchFragment_to_movieDetailFragment,
                bundle
            )
        }
    }

    override fun observeData() {
        viewModel.searchMovies.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movieResponse ->
                        movieAdapter.differ.submitList(movieResponse.results.map {
                            MovieApplication.genreList?.let { it1 ->
                                it.fillGenre(
                                    it1
                                )
                            }
                        })
                        val totalPages = movieResponse.total_results / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchMoviePage == totalPages
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
                viewModel.searchMovies(etSearch.text.toString())
                isScrolling = false
            } else {
                rvMovieSearch.setPadding(0, 0, 0, 0)
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
        rvMovieSearch.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@MovieSearchFragment.scrollListener)
        }
    }


}