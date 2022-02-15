package com.example.luminosity.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.luminosity.R
import com.example.luminosity.adapters.FeedAdapter
import com.example.luminosity.databinding.FragmentPhotoDetailBinding
import com.example.luminosity.models.Photo
import com.example.luminosity.models.PhotoDetails
import com.example.luminosity.utils.AutoClearedValue
import com.example.luminosity.ui.viewmodels.FeedViewModel
import com.example.luminosity.ui.viewmodels.PhotoDetailsViewModel
import com.example.scopedstorage.utils.ViewBindingFragment
import com.skillbox.github.utils.showSnackServerConnectError
import com.skillbox.github.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoDetailsFragment: ViewBindingFragment<FragmentPhotoDetailBinding>(FragmentPhotoDetailBinding::inflate) {

    private val viewModel by viewModels<PhotoDetailsViewModel>()
    private val args: PhotoDetailsFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPhotoDetails(args.photoId)
            println{args.photoId}
            bindViewModel()
        }
    }

    private fun bindViewModel() {
        viewModel.photo.observe(viewLifecycleOwner, ::showDetailsAboutPhoto)
        viewModel.progressBarVisible.observe(viewLifecycleOwner) {
            binding.photoDetailsProgressBar.isVisible = it
        }
        viewModel.serverConnectError.observe(viewLifecycleOwner) {
            showSnackServerConnectError(binding.root, requireContext())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDetailsAboutPhoto(photo: PhotoDetails){
        val tagsList = mutableListOf<String>()
        photo.tags.forEach {
            tagsList.add(it.title)
        }
        if (photo.liked) {
            binding.likePhotoButton.isVisible = true
            binding.unlikePhotoButton.isVisible = false
        } else {
            binding.likePhotoButton.isVisible = false
            binding.unlikePhotoButton.isVisible = true
        }
        with(binding){
            likeCount.text = photo.likes.toString()
            authorName.text = photo.author.username
            locationTextView.text = photo.location.city + ", " + photo.location.country
            tagsTextView.text = tagsList.joinToString(" #", "#")
                madeWithTextView.text = resources.getString(R.string.made_with) + photo.exif.make
                modelTextView.text = resources.getString(R.string.model) + photo.exif.model
                exposureTextView.text = resources.getString(R.string.exposure) + photo.exif.exposure_time
                apertureTextView.text = resources.getString(R.string.aperture) + photo.exif.aperture
                focalLengthTextView.text = resources.getString(R.string.focal_length) + photo.exif.focal_length
                isoTextView.text = resources.getString(R.string.iso) + photo.exif.iso.toString()
                aboutAuthorTextView.text = resources.getString(R.string.about_author) + photo.author.username + ":"
                authorDescriptionTextView.text = photo.author.bio
                downloadTextView.text = resources.getString(R.string.download) + photo.downloads.toString() + ")"
            }


        Glide.with(requireContext())
            .load(photo.author.profile_image.profile_small)
            .error(R.drawable.avatar_nobody)
            .into(binding.profileImage)

        Glide.with(requireContext())
            .load(photo.urls.small)
            .error(R.drawable.no_image)
            .into(binding.imageView)

        initButtonsListeners(photo)
        }

    private fun initButtonsListeners(photo: PhotoDetails) {
        binding.unlikePhotoButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                viewModel.postLikeToPhoto(photo.id, viewLifecycleOwner.lifecycleScope)
            }

        }
        binding.likePhotoButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                viewModel.deleteLikeToPhoto(photo.id, viewLifecycleOwner.lifecycleScope)
            }

        }
        binding.profileImage.setOnClickListener {
            if (photo != null) {
                openAuthorProfile(photo)
            }
        }
        binding.authorLogin.setOnClickListener {
            if (photo != null) {
                openAuthorProfile(photo)
            }
        }
        binding.authorName.setOnClickListener {
            if (photo != null) {
                openAuthorProfile(photo)
            }
        }
        binding.locationTextView.setOnClickListener {

        }
        binding.downloadTextView.setOnClickListener {

        }
    }

    private fun openAuthorProfile(photo: PhotoDetails) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://unsplash.com/@${photo.author.username}?utm_source=Luminosity&utm_medium=referral")
        )
        startActivity( browserIntent, null)
    }
    }
