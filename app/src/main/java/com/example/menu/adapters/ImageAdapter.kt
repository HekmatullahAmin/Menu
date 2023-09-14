package com.example.menu.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.menu.R
import com.example.menu.databinding.ItemImageListViewBinding
import com.example.menu.network.Item

class ImageAdapter(private val itemList: List<Item>, val onItemClicked: (Item) -> Unit) :
    RecyclerView.Adapter<ImageAdapter.ImageVieHolder>() {

    class ImageVieHolder(private val binding: ItemImageListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            val imageUri = item.urls.raw.toUri().buildUpon().scheme("https").build()
            binding.itemImageView.load(imageUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
//            binding.executePendingBindings()  not working??
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageVieHolder {
        val layout =
            ItemImageListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageVieHolder(layout)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ImageVieHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }
}