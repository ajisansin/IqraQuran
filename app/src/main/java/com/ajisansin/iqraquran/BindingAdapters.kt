package com.ajisansin.iqraquran

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajisansin.iqraquran.data.source.QuranEntity
import com.ajisansin.iqraquran.ui.QuranApiStatus
import com.ajisansin.iqraquran.ui.QuranListAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<QuranEntity>?) {
    val adapter = recyclerView.adapter as QuranListAdapter
    adapter.submitList(data)
}

@BindingAdapter("apiStatus")
fun bindStatus(progressBar: ProgressBar, status: QuranApiStatus?) {
    when(status) {
        QuranApiStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        QuranApiStatus.DONE -> {
            progressBar.visibility = View.GONE
        }
        QuranApiStatus.ERROR -> {
            progressBar.visibility = View.VISIBLE
        }
        else -> {}
    }
}