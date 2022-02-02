package com.example.luminosity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.luminosity.databinding.CollectionItemBinding
import com.example.luminosity.models.CollectionPhoto
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate


class CollectionsDelegateAdapter(
    private val onItemCliked: (position: Int) -> Unit,
):
    AbsListItemAdapterDelegate<CollectionPhoto, CollectionPhoto, CollectionsDelegateAdapter.RepositoryHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup): RepositoryHolder {
        return RepositoryHolder(onItemCliked, CollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun isForViewType(
        item: CollectionPhoto,
        items: MutableList<CollectionPhoto>,
        position: Int
    ): Boolean {
        return true
    }

    override fun onBindViewHolder(
        item: CollectionPhoto,
        holder: RepositoryHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }


    class RepositoryHolder(
        onItemCliked: (position: Int) -> Unit,
        binding: CollectionItemBinding
        ): RecyclerView.ViewHolder(binding.root){
//        val nameTextView: TextView = binding.nameTextView
//        val priceTextView: TextView = binding.priceTextView
//        val photoImage: ImageView = binding.photo

        init {
            binding.root.setOnClickListener {
                onItemCliked(absoluteAdapterPosition)
            }
        }

        fun bind (collection: CollectionPhoto) {
//            nameTextView.text = contact.name
//            priceTextView.text = contact.price + " руб."
//            photoImage.setImageResource(contact.photo)
        }

    }


}