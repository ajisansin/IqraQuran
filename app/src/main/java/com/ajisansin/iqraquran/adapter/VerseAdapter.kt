package com.ajisansin.iqraquran.adapter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajisansin.iqraquran.data.source.VerseEntity
import com.ajisansin.iqraquran.databinding.ItemVerseListBinding

class VerseAdapter(
    private val listVerses: List<VerseEntity>
) : RecyclerView.Adapter<VerseAdapter.VerseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseViewHolder {
        val binding =
            ItemVerseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VerseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VerseViewHolder, position: Int) {
        val verse: VerseEntity = listVerses[position]
        holder.bind(verse)
    }

    override fun getItemCount(): Int {
        return listVerses.size
    }

    class VerseViewHolder(private val binding: ItemVerseListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(verse: VerseEntity) {
            binding.tvArabic.text = verse.arab
            binding.tvNomorAyat.text =(verse.nomor)
            binding.tvTerjemahan.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(verse.terjemahan, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(verse.terjemahan)
            }
        }

    }
}