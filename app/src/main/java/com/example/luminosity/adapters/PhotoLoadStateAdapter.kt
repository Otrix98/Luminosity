package com.example.luminosity.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.luminosity.R
import com.example.luminosity.databinding.PhotoLoadStateItemBinding

class PhotoLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PhotoLoadStateAdapter.PhotoLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: PhotoLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PhotoLoadStateViewHolder {
        return PhotoLoadStateViewHolder.create(parent, retry)
    }

    class PhotoLoadStateViewHolder(
        private val binding: PhotoLoadStateItemBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): PhotoLoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.photo_load_state_item, parent, false)
                val binding = PhotoLoadStateItemBinding.bind(view)
                return PhotoLoadStateViewHolder(binding, retry)
            }
        }
    }
}