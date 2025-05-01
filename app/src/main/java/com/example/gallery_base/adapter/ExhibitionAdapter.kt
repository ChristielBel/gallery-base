package com.example.gallery_base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.databinding.ElementExhibitionListBinding

class ExhibitionAdapter : ListAdapter<Exhibition, ExhibitionAdapter.ExhibitionViewHolder>(DiffCallback) {

    inner class ExhibitionViewHolder(private val binding: ElementExhibitionListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exhibition: Exhibition) {
            binding.tvExhibition.text = exhibition.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionViewHolder {
        val binding = ElementExhibitionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExhibitionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExhibitionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<Exhibition>() {
        override fun areItemsTheSame(oldItem: Exhibition, newItem: Exhibition) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Exhibition, newItem: Exhibition) = oldItem == newItem
    }
}
