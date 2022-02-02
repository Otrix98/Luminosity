package com.example.luminosity.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.luminosity.R
import com.example.luminosity.adapters.AdaptersListener
import com.example.luminosity.adapters.FeedAdapter
import com.example.luminosity.adapters.PhotoComparator
import com.example.luminosity.data.PhotoRepository
import com.example.luminosity.databinding.FragmentFeedBinding
import com.example.luminosity.models.Photo
import com.example.luminosity.networking.Network
import com.example.luminosity.ui.viewmodels.FeedViewModel
import com.example.scopedstorage.utils.ViewBindingFragment
import com.skillbox.github.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class FeedFragment: ViewBindingFragment<FragmentFeedBinding>(FragmentFeedBinding::inflate), AdaptersListener {

    private val viewModel by viewModels<FeedViewModel>()

    private var pagingAdapter = FeedAdapter(
        PhotoComparator
    )

    private lateinit var thisView: View

    override fun onPause() {
        super.onPause()
        pagingAdapter.removeLoadStateListener { }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisView = view
        initList()
        bindViewModel()
        pagingAdapter.setOnClickListener(this)
    }

    override fun onClickItem(photo: Photo) {
        val action = FeedFragmentDirections.actionFeedFragmentToPhotoDetailsFragment()
        findNavController().navigate(action)
    }

    override fun onClickLike(photo: Photo, position: Int) {
        Toast.makeText(requireContext(), "Like clicked ${photo.liked}", Toast.LENGTH_SHORT).show()
        if (!photo.liked) {
            postLike(photo.id)
            pagingAdapter.notifyItemChanged(position)
        } else {
            deleteLike(photo.id)
        }
    }

    override fun onClickInfo(photo: Photo) {
        openAuthorProfile(photo)
    }

    private fun bindViewModel() {
        with(viewModel) {
            progressBarVisible.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
            serverConnectError.observe(viewLifecycleOwner) {
            showSnackServerConnectError(thisView, requireContext())
        }
            addRepeatingJob(Lifecycle.State.STARTED) {
                searchResults.collectLatest(pagingAdapter::submitData)
            }

        }
    }

    @ExperimentalPagingApi
    private fun initList() {
            with(binding.recyclerView) {
                setHasFixedSize(true)
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = pagingAdapter
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }
        }

    private fun openAuthorProfile(photo: Photo) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://unsplash.com/@${photo.author.username}?utm_source=Luminosity&utm_medium=referral")
        )
        startActivity( browserIntent, null)
    }

    private fun postLike(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val code = viewModel.postLikeToPhoto(id, viewLifecycleOwner.lifecycleScope)
            if (code == 201) {
            } else {
                showSnackServerConnectError(thisView, requireContext())
            }
        }
    }

    private fun deleteLike(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val code = viewModel.deleteLikeToPhoto(id, viewLifecycleOwner.lifecycleScope)
            if (code == 200) {
            } else {
                showSnackServerConnectError(thisView, requireContext())
            }
        }
    }

//    private fun openLinkPhotoInWeb(position: Int) {
//        val url = viewModel.getPhotoByPosition(position, IMAGES_FEED_FRAGMENT)?.links?.html
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//        startActivity(intent)
//    }
//
//    private fun shareLinksToPhoto(position: Int) {
//        val url = viewModel.getPhotoByPosition(position, IMAGES_FEED_FRAGMENT)?.links?.html
//        val sendIntent = Intent()
//        sendIntent.action = Intent.ACTION_SEND
//        sendIntent.putExtra(Intent.EXTRA_TEXT, url)
//        sendIntent.type = TEXT_PLAIN
//        startActivity(
//            Intent.createChooser(
//                sendIntent,
//                resources.getString(R.string.share_image_link)
//            )
//        )
//    }

//    private fun downloadImage(position: Int) {
//        viewLifecycleOwner.lifecycleScope.launch {
//            if (!isInternetAvailable(requireContext())) {
//                showSnackServerConnectError(thisView, requireContext())
//                return@launch
//            }
//            val id = viewModel.getPhotoByPosition(position, IMAGES_FEED_FRAGMENT)?.id
//            val urlDownloadFile = Network.unsplashApi.trackDownloadPhoto(id!!).url
//            val nameNewSaveFile = "$id.jpg"
//            viewModel.loadImageAndSaveToScopedStorage(
//                nameNewSaveFile,
//                urlDownloadFile
//            )
//        }
//    }
}