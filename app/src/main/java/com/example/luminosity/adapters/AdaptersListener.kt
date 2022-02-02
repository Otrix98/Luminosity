package com.example.luminosity.adapters

import com.example.luminosity.models.Photo

interface AdaptersListener {
    fun onClickItem(photo: Photo)
    fun onClickLike(photo: Photo, position: Int)
    fun onClickInfo(photo: Photo)
}