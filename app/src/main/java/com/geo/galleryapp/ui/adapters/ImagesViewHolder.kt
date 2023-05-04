package com.geo.galleryapp.ui.adapters

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.geo.galleryapp.R
import com.geo.galleryapp.databinding.ImageItemBinding
import com.geo.galleryapp.models.ImageData
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class ImagesViewHolder(binding: ImageItemBinding)
    : RecyclerView.ViewHolder(binding.root) {
    private val imageView: ImageView = binding.imageView
    private val clRefresh: ConstraintLayout = binding.clRefresh
    private val icRefresh: ImageView = binding.icRefresh
    private val progressBar: ProgressBar = binding.progressBar

    fun bind(imageData: ImageData?) {
        showImage(imageData)
        clRefresh.setOnClickListener {
            showImage(imageData)
        }
    }

    private fun showImage(imageData: ImageData?) {
        icRefresh.visibility = GONE
        progressBar.visibility = VISIBLE
        Picasso.get()
            .load(imageData?.assets?.preview?.url)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    clRefresh.visibility = GONE
                }

                override fun onError(e: Exception?) {
                    icRefresh.visibility = VISIBLE
                    progressBar.visibility = GONE
                }
            })
    }

    companion object {
        fun create(parent: ViewGroup): ImagesViewHolder {
            return ImagesViewHolder(
                ImageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}