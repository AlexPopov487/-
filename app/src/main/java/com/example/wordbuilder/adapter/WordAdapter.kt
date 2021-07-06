package com.example.wordbuilder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wordbuilder.databinding.WordItemBinding
import com.example.wordbuilder.db.WordEntity

class WordAdapter: ListAdapter<String, WordViewHolder>(DiffCallback) {
    companion object DiffCallback: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            // we don't have unique ids, to checking by length will do
            return oldItem.length == newItem.length
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
            val binding = WordItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return WordViewHolder(binding)
        }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class WordViewHolder(private val binding: WordItemBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(word: String) {
        binding.tVWord.text = word
    }

}
