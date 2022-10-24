package com.geo.galleryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.geo.galleryapp.R
import com.geo.galleryapp.databinding.ImageItemBinding
import com.geo.galleryapp.models.ImageData
import com.geo.galleryapp.other.GlideRequests
import com.squareup.picasso.Picasso

class ImagesViewHolder(binding: ImageItemBinding, private val glide: GlideRequests)
    : RecyclerView.ViewHolder(binding.root) {
    private val imageView: ImageView = binding.imageView
//    private val text: TextView = binding.text

    fun bind(imageData: ImageData?) {
        Picasso.get()
            .load(imageData?.assets?.preview?.url)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(imageView);
//        glide.load(imageData?.assets?.preview?.url).into(imageView)
//        text.text = imageData?.description
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests): ImagesViewHolder {
            return ImagesViewHolder(
                ImageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                glide
            )
        }
    }
}