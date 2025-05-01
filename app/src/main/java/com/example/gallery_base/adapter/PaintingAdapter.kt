package com.example.gallery_base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_base.data.Painting
import com.example.gallery_base.databinding.ItemPaintingBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PaintingAdapter(
    private val onItemClick: (Painting) -> Unit
) : ListAdapter<Painting, PaintingAdapter.PaintingViewHolder>(DiffCallback) {

    inner class PaintingViewHolder(private val binding: ItemPaintingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(painting: Painting) {
            binding.tvPaintingTitle.text = painting.title

            binding.root.setOnClickListener {
                onItemClick(painting)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintingViewHolder {
        val binding = ItemPaintingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaintingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaintingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<Painting>() {
        override fun areItemsTheSame(oldItem: Painting, newItem: Painting) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Painting, newItem: Painting) = oldItem == newItem
    }
}