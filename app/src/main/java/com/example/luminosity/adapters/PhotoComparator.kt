package com.example.luminosity.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.luminosity.models.Photo

object PhotoComparator:  DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}