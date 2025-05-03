package com.example.gallery_base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_base.R
import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.databinding.ItemExhibitionBinding
import java.util.UUID

class ExhibitionAdapter(
    private val onItemClick: (Exhibition) -> Unit,
    private val onItemLongClick: (Exhibition) -> Unit
) : ListAdapter<Exhibition, ExhibitionAdapter.ExhibitionViewHolder>(DiffCallback) {

    private var selectedId: UUID? = null

    fun setSelectedId(id: UUID?) {
        val oldId = selectedId
        selectedId = id

        // Используем snapshot списка
        val snapshot = currentList

        val oldIndex = snapshot.indexOfFirst { it.id == oldId }
        val newIndex = snapshot.indexOfFirst { it.id == id }

        if (oldIndex != -1) notifyItemChanged(oldIndex)
        if (newIndex != -1 && newIndex != oldIndex) notifyItemChanged(newIndex)
    }

    inner class ExhibitionViewHolder(private val binding: ItemExhibitionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exhibition: Exhibition) {
            binding.tvExhibition.text = exhibition.title

            if (exhibition.id == selectedId) {
                binding.root.setBackgroundResource(R.drawable.selected_background)
            } else {
                binding.root.setBackgroundResource(android.R.color.transparent)
            }

            binding.root.setOnClickListener {
                onItemClick(exhibition)
                setSelectedId(exhibition.id)
            }

            binding.root.setOnLongClickListener {
                onItemLongClick(exhibition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionViewHolder {
        val binding = ItemExhibitionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
