package com.example.gallery_base.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gallery_base.R
import com.example.gallery_base.adapter.PaintingAdapter
import com.example.gallery_base.databinding.FragmentPaintingBinding
import java.util.UUID
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gallery_base.MyApplication
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PaintingFragment : Fragment() {

    companion object {
        private const val ARG_ARTIST_ID = "artist_id"

        fun newInstance(artistId: UUID): PaintingFragment {
            val fragment = PaintingFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(ARG_ARTIST_ID, artistId)
            }
            return fragment
        }
    }

    private lateinit var binding: FragmentPaintingBinding
    private lateinit var adapter: PaintingAdapter
    private val viewModel: PaintingViewModel by viewModels {
        PaintingViewModelFactory((requireActivity().application as MyApplication).paintingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaintingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PaintingAdapter { painting ->
            // Здесь можно добавить обработку клика по картине, если нужно
        }

        binding.rvPainting.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPainting.adapter = adapter

        val artistId = requireArguments().getSerializable(ARG_ARTIST_ID) as UUID

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPaintingsByArtist(artistId).collect { paintings ->
                    adapter.submitList(paintings)
                }
            }
        }

        binding.fabAppendPainting.setOnClickListener {
            // Реализовать добавление новой картины
        }
    }
}