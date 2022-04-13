package com.ajisansin.iqraquran.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ajisansin.iqraquran.R
import com.ajisansin.iqraquran.databinding.FragmentQuranListBinding

class QuranListFragment : Fragment() {

    private val viewModel: QuranViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentQuranListBinding.inflate(inflater)
        viewModel.getQuranList()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.rvSurah.adapter = QuranListAdapter(QuranListener { quran ->
            viewModel.onQuranClicked(quran)
            findNavController()
                .navigate(R.id.action_quranListFragment_to_quranDetailFragment)
        })

        // Inflate the layout for this fragment
        return binding.root
    }
}