package com.ajisansin.iqraquran.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajisansin.iqraquran.adapter.VerseAdapter
import com.ajisansin.iqraquran.data.source.VerseEntity
import com.ajisansin.iqraquran.databinding.FragmentQuranDetailBinding
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.util.Util
import org.json.JSONArray
import org.json.JSONException

class QuranDetailFragment : Fragment() {

    private val viewModel: QuranViewModel by activityViewModels()
    private var player: ExoPlayer? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private lateinit var binding: FragmentQuranDetailBinding
    private lateinit var verseAdapter: VerseAdapter
    private var y = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentQuranDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.ivReminder.setOnClickListener {
            activity?.let { it1 ->
                viewModel.quran.value?.let { it2 -> ReminderDialogFragment(it2.nama) }
                    ?.show(it1.supportFragmentManager, "QuranReminderDialogFragment")
            }
        }

        binding.rvAyat.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    binding.fabTop.visibility = View.GONE
                }
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    if (y <= 0) {
                        binding.fabTop.visibility = View.VISIBLE
                    } else {
                        binding.fabTop.visibility = View.GONE
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                y = dy
            }
        })

        binding.fabTop.setOnClickListener {
            binding.nestedScroll.smoothScrollTo(0, 0)
            binding.fabTop.visibility = View.GONE
        }

        getListVerse()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initializePlayer() {
        player = context?.let {
            ExoPlayer.Builder(it)
                .build()
                .also { exoPlayer ->
                    binding.audioPlayer.player = exoPlayer
                }
        }
        val mediaItem: MediaItem? = viewModel.quran.value?.let { MediaItem.fromUri(it.audio) }
        if (mediaItem != null) {
            player?.setMediaItem(mediaItem)
        }
        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare()
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun getListVerse() {
        binding.progressBar.visibility = View.VISIBLE
        val queue: RequestQueue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET,
            "https://api.npoint.io/99c279bb173a6e28359c/surat/${viewModel.quran.value?.nomor}",
            { res: String -> parseJSON(res) }) {
            Toast.makeText(
                context,
                "Connection error please retry",
                Toast.LENGTH_SHORT
            ).show()
        }
        queue.add(stringRequest)
    }

    private fun parseJSON(res: String) {
        binding.progressBar.visibility = View.INVISIBLE
        try {
            val jsonArray = JSONArray(res)
            val verseArrayList = ArrayList<VerseEntity>()
            for (i in 0 until jsonArray.length()) {
                val verseEntity = VerseEntity()
                val jsonObject = jsonArray.getJSONObject(i)
                verseEntity.arab = jsonObject["ar"].toString()
                verseEntity.indo = jsonObject["id"].toString()
                verseEntity.terjemahan = jsonObject["tr"].toString()
                verseEntity.nomor = jsonObject["nomor"].toString()
                verseArrayList.add(verseEntity)
            }

            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
            binding.rvAyat.layoutManager = layoutManager
            verseAdapter = VerseAdapter(verseArrayList)
            binding.rvAyat.adapter = verseAdapter

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}