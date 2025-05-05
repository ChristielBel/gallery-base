package com.example.gallery_base.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gallery_base.data.Artist
import com.example.gallery_base.data.Painting
import com.example.gallery_base.databinding.FragmentPaintingBinding
import com.example.gallery_base.fragment.PaintingFragment
import com.example.gallery_base.fragment.PaintingViewModel
import kotlinx.coroutines.launch
import java.util.UUID

class ArtistAdapter(
    fragment: Fragment,
    private val artists: List<Artist>,
    private val onArtistSelected: (Artist) -> Unit
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = artists.size

    override fun createFragment(position: Int): Fragment {
        return PaintingFragment.newInstance(artists[position].id)
    }

    class ArtistPaintingsFragment : Fragment() {
        private var _binding: FragmentPaintingBinding? = null
        private val binding get() = _binding!!
        private lateinit var paintingAdapter: PaintingAdapter
        private val viewModel: PaintingViewModel by viewModels(ownerProducer = { requireParentFragment() })
        private lateinit var artistId: UUID  // Теперь храним только ID
        private var paintingActionListener: PaintingActionListener? = null

        fun setPaintingActionListener(listener: PaintingActionListener) {
            this.paintingActionListener = listener
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            artistId = requireArguments().getSerializable("artist_id") as UUID
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentPaintingBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            paintingAdapter = PaintingAdapter(
                onLongClick = { painting, view ->
                    paintingActionListener?.onPaintingLongClick(painting, view)
                },
                onEditClick = { painting ->
                    paintingActionListener?.onPaintingEditClick(painting)
                },
                onDeleteClick = { painting ->
                    paintingActionListener?.onPaintingDeleteClick(painting)
                }
            )

            binding.rvPainting.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = paintingAdapter
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getPaintingsByArtist(artistId).collect { paintings ->  // Используем artistId
                        paintingAdapter.submitList(paintings)
                    }
                }
            }

            binding.fabAppendPainting.setOnClickListener {
                paintingActionListener?.onAddPaintingClick(artistId)  // Используем artistId
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        companion object {
            fun newInstance(artistId: UUID): ArtistPaintingsFragment {
                return ArtistPaintingsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable("artist_id", artistId)
                    }
                }
            }
        }
    }

    interface PaintingActionListener {
        fun onPaintingLongClick(painting: Painting, view: View)
        fun onPaintingEditClick(painting: Painting)
        fun onPaintingDeleteClick(painting: Painting)
        fun onAddPaintingClick(artistId: UUID)
    }
}