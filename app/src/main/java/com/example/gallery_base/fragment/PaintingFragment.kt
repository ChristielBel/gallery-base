package com.example.gallery_base.fragment

import android.app.AlertDialog
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
import com.example.gallery_base.MyApplication
import com.example.gallery_base.R
import com.example.gallery_base.adapter.PaintingAdapter
import com.example.gallery_base.data.Painting
import com.example.gallery_base.databinding.FragmentPaintingBinding
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class PaintingFragment : Fragment() {

    companion object {
        private const val ARG_ARTIST_ID = "artist_id"

        fun newInstance(artistId: UUID): PaintingFragment {
            return PaintingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ARTIST_ID, artistId.toString())
                }
            }
        }
    }

    private lateinit var binding: FragmentPaintingBinding
    private lateinit var adapter: PaintingAdapter
    private val viewModel: PaintingViewModel by viewModels {
        PaintingViewModelFactory((requireActivity().application as MyApplication).appContainer.paintingRepository)
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

        adapter = PaintingAdapter(
            onLongClick = { painting, buttonsView ->
                buttonsView.visibility = View.VISIBLE
            },
            onEditClick = { painting ->
                showEditPaintingDialog(painting)
            },
            onDeleteClick = { painting ->
                showDeletePaintingDialog(painting)
            }
        )

        binding.rvPainting.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvPainting.adapter = adapter

        val artistIdString = requireArguments().getString(ARG_ARTIST_ID)
        val artistId = UUID.fromString(
            artistIdString ?: throw IllegalArgumentException("Artist ID must not be null")
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPaintingsByArtist(artistId).collect { paintings ->
                    adapter.submitList(paintings)
                }
            }
        }

        binding.fabAppendPainting.setOnClickListener {
            showAddPaintingDialog(artistId)
        }
    }

    private fun showAddPaintingDialog(artistId: UUID) {
        val inputFragment = PaintingInputFragment.newInstance("", "")
        inputFragment.setPaintingListener(object : PaintingInputFragment.PaintingListener {
            override fun onPaintingSaved(title: String, description: String, date: Long) {
                val newPainting = Painting(
                    title = title,
                    description = description,
                    dateOfWriting = Date(date),
                    artistId = artistId
                )
                lifecycleScope.launch {
                    viewModel.insertPainting(newPainting)
                }
            }
        })

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fcMain, inputFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showEditPaintingDialog(painting: Painting) {
        val inputFragment = PaintingInputFragment.newInstance(
            painting.title,
            painting.description ?: ""
        )
        inputFragment.setPaintingListener(object : PaintingInputFragment.PaintingListener {
            override fun onPaintingSaved(title: String, description: String, date: Long) {
                val updated = painting.copy(
                    title = title,
                    description = description,
                    dateOfWriting = Date(date)
                )
                lifecycleScope.launch {
                    viewModel.updatePainting(updated)
                }
            }
        })

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fcMain, inputFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showDeletePaintingDialog(painting: Painting) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удалить картину")
            .setMessage("Вы действительно хотите удалить картину \"${painting.title}\"?")
            .setPositiveButton("Да") { _, _ ->
                lifecycleScope.launch {
                    viewModel.deletePainting(painting)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}