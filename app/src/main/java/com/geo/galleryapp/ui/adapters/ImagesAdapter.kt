package com.geo.galleryapp.ui.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.geo.galleryapp.models.ImageData

/**
 * [RecyclerView.Adapter] that can display a [ImageData].
 */
class ImagesAdapter()
    : PagingDataAdapter<ImageData, ImagesViewHolder>(ImageComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        return ImagesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ImageComparator : DiffUtil.ItemCallback<ImageData>() {
    override fun areItemsTheSame(
        oldItem: ImageData,
        newItem: ImageData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ImageData,
        newItem: ImageData
    ): Boolean {
        return oldItem == newItem
    }
}