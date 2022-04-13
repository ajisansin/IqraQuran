package com.ajisansin.iqraquran.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ajisansin.iqraquran.data.source.QuranEntity
import com.ajisansin.iqraquran.databinding.ItemSurahListBinding

class QuranListAdapter(val clickListener: QuranListener) :
    ListAdapter<QuranEntity, QuranListAdapter.QuranViewHolder>(DiffCallback) {

    class QuranViewHolder(
        var binding: ItemSurahListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: QuranListener, quran: QuranEntity) {
            binding.quran = quran
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<QuranEntity>() {

        override fun areItemsTheSame(oldItem: QuranEntity, newItem: QuranEntity): Boolean {
            return oldItem.nomor == newItem.nomor
        }

        override fun areContentsTheSame(oldItem: QuranEntity, newItem: QuranEntity): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return QuranViewHolder(
            ItemSurahListBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: QuranViewHolder, position: Int) {
        val quran = getItem(position)
        holder.bind(clickListener, quran)
    }
}

class QuranListener(val clickListener: (quran: QuranEntity) -> Unit) {
    fun onClick(quran: QuranEntity) = clickListener(quran)
}