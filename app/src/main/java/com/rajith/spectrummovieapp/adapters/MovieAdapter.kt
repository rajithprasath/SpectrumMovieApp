package com.rajith.spectrummovieapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rajith.spectrummovieapp.R
import com.rajith.spectrummovieapp.models.Result
import com.rajith.spectrummovieapp.util.Constants.Companion.IMAGE_BASE_URL
import kotlinx.android.synthetic.main.activity_movies.view.*
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(IMAGE_BASE_URL+movie.poster_path).into(ivPoster)
            tvTitle.text = movie.title
            tvReleaseDate.text = movie.release_date
            tvVoteCount.text = movie.vote_count.toString()
            setOnClickListener {
                onItemClickListener?.let { it(movie) }
            }
        }


    }

    private var onItemClickListener: ((Result) -> Unit)? = null

    fun setOnItemClickListener(listener : (Result) -> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallBack)
}