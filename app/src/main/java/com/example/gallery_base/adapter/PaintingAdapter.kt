package com.example.gallery_base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_base.data.Painting
import com.example.gallery_base.databinding.ItemPaintingBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PaintingAdapter(
    private val onLongClick: (Painting, View) -> Unit,
    private val onEditClick: (Painting) -> Unit,
    private val onDeleteClick: (Painting) -> Unit
) : ListAdapter<Painting, PaintingAdapter.PaintingViewHolder>(PaintingDiffCallback()) {

    inner class PaintingViewHolder(private val binding: ItemPaintingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(painting: Painting) {
            binding.tvPaintingTitle.text = painting.title

            binding.llPaintingButtons.visibility = View.GONE

            // Долгое нажатие для показа кнопок
            binding.root.setOnLongClickListener {
                onLongClick(painting, binding.llPaintingButtons)
                true
            }

            // Короткое нажатие - скрываем кнопки
            binding.root.setOnClickListener {
                binding.llPaintingButtons.visibility = View.GONE
            }

            binding.ibEditPainting.setOnClickListener {
                onEditClick(painting)
                binding.llPaintingButtons.visibility = View.GONE
            }

            binding.ibDeletePainting.setOnClickListener {
                onDeleteClick(painting)
                binding.llPaintingButtons.visibility = View.GONE
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

    class PaintingDiffCallback : DiffUtil.ItemCallback<Painting>() {
        override fun areItemsTheSame(oldItem: Painting, newItem: Painting) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Painting, newItem: Painting) =
            oldItem == newItem
    }
}