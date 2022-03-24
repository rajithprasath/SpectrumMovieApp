package com.rajith.spectrummovieapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajith.spectrummovieapp.view.activities.MoviesListActivity
import com.rajith.spectrummovieapp.view.adapters.MovieAdapter
import com.rajith.spectrummovieapp.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_now_playing.*

abstract class BaseFragment: Fragment() {

    lateinit var viewModel: MoviesViewModel
    lateinit var movieAdapter: MovieAdapter

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = (activity as MoviesListActivity).viewModel
        return inflater.inflate(getLayoutId(), container, false)
    }

    open fun hideProgressBar() {}

    open fun showProgressBar() {}

    open fun setupRecyclerView() {}

    open fun observeData() {}
}