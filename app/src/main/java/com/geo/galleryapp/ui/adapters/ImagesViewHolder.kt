package com.geo.galleryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.geo.galleryapp.R
import com.geo.galleryapp.databinding.ImageItemBinding
import com.geo.galleryapp.models.ImageData
import com.squareup.picasso.Picasso

class ImagesViewHolder(binding: ImageItemBinding)
    : RecyclerView.ViewHolder(binding.root) {
    private val imageView: ImageView = binding.imageView

    fun bind(imageData: ImageData?) {
        Picasso.get()
            .load(imageData?.assets?.preview?.url)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(imageView)
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