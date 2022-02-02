package com.example.luminosity.ui

import androidx.fragment.app.viewModels
import com.example.luminosity.adapters.FeedAdapter
import com.example.luminosity.databinding.FragmentPhotoDetailBinding
import com.example.luminosity.utils.AutoClearedValue
import com.example.luminosity.ui.viewmodels.FeedViewModel
import com.example.scopedstorage.utils.ViewBindingFragment

class PhotoDetailsFragment: ViewBindingFragment<FragmentPhotoDetailBinding>(FragmentPhotoDetailBinding::inflate) {

    private var contactsAdapter: FeedAdapter by AutoClearedValue(this)

    private val viewModel: FeedViewModel by viewModels()
}