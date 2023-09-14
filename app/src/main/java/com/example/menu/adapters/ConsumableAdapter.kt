package com.example.menu.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.menu.R
import com.example.menu.data.Consumable
import com.example.menu.databinding.ConsumableListViewBinding

class ConsumableAdapter(
    val onConsumableClicked: (Consumable) -> Unit,
    val onEditClicked: (Consumable) -> Unit,
    val onDeleteClicked: (Consumable) -> Unit
) :
    ListAdapter<Consumable, ConsumableAdapter.ConsumableViewHolder>(
        DiffCallback
    ) {
    class ConsumableViewHolder(private val binding: ConsumableListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(consumable: Consumable) {
            binding.apply {
                consumableNameTV.text = consumable.name
                binding.consumablePriceTV.text = consumable.price.toString()
                binding.consumableImageView.load(consumable.imageUrl) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumableViewHolder {
        val layout =
            ConsumableListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConsumableViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ConsumableViewHolder, position: Int) {
        val consumable = getItem(position)
        holder.bind(consumable)
        holder.itemView.setOnClickListener {
            onConsumableClicked(consumable)
        }
        holder.itemView.findViewById<ImageView>(R.id.deleteConsumable).setOnClickListener {
            onDeleteClicked(consumable)
        }
        holder.itemView.findViewById<ImageView>(R.id.editConsumable).setOnClickListener {
            onEditClicked(consumable)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Consumable>() {
            override fun areItemsTheSame(
                oldConsumable: Consumable,
                newConsumable: Consumable
            ): Boolean {
                return oldConsumable == newConsumable
            }

            override fun areContentsTheSame(
                oldConsumable: Consumable,
                newConsumable: Consumable
            ): Boolean {
                return oldConsumable.id == newConsumable.id
            }
        }
    }
}