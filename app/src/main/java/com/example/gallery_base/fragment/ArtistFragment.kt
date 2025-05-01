package com.example.gallery_base.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gallery_base.MainActivity
import com.example.gallery_base.MyApplication
import com.example.gallery_base.R
import com.example.gallery_base.adapter.ArtistAdapter
import com.example.gallery_base.data.Artist
import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.databinding.FragmentArtistBinding
import kotlinx.coroutines.launch
import java.util.UUID

class ArtistFragment : Fragment(), MainActivity.Edit {

    companion object {
        private const val ARG_EXHIBITION_ID = "exhibition_id"

        fun newInstance(exhibitionId: UUID): ArtistFragment {
            val fragment = ArtistFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(ARG_EXHIBITION_ID, exhibitionId)
            }
            return fragment
        }
    }

    private lateinit var binding: FragmentArtistBinding
    private lateinit var adapter: ArtistAdapter
    private val viewModel: ArtistViewModel by viewModels {
        ArtistViewModelFactory((requireActivity().application as MyApplication).artistRepository)
    }
    private var selectedArtist: Artist? = null
    private lateinit var exhibitionId: UUID

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        exhibitionId = requireArguments().getSerializable(ARG_EXHIBITION_ID) as UUID

        adapter = ArtistAdapter { artist ->
            selectedArtist = artist
            parentFragmentManager.beginTransaction()
                .replace(R.id.fcMain, PaintingFragment.newInstance(artist.id))
                .addToBackStack(null)
                .commit()
        }



        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        val exhibitionId = requireArguments().getSerializable(ARG_EXHIBITION_ID) as UUID

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getArtistsByExhibition(exhibitionId).collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun append() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_string, null)
        val editText = dialogView.findViewById<EditText>(R.id.etString)
        val textView = dialogView.findViewById<TextView>(R.id.tvInfo)
        textView.text = "Введите ФИО нового художника"

        AlertDialog.Builder(requireContext())
            .setTitle("Добавить художника")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val input = editText.text.toString()
                if (input.isNotBlank()) {
                    val newArtist = Artist(name = input, exhibitionId = exhibitionId)
                    lifecycleScope.launch {
                        viewModel.insertArtist(newArtist)
                    }
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }


    override fun update() {
        val current = selectedArtist
        if (current != null) {
            val dialogView = layoutInflater.inflate(R.layout.dialog_string, null)
            val editText = dialogView.findViewById<EditText>(R.id.etString)
            val textView = dialogView.findViewById<TextView>(R.id.tvInfo)
            textView.text = "Измените ФИО художника"
            editText.setText(current.name)

            AlertDialog.Builder(requireContext())
                .setTitle("Обновить художника")
                .setView(dialogView)
                .setPositiveButton("Сохранить") { _, _ ->
                    val updatedName = editText.text.toString()
                    if (updatedName.isNotBlank()) {
                        val updated = current.copy(name = updatedName)
                        lifecycleScope.launch {
                            viewModel.updateArtist(updated)
                        }
                    }
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }


    override fun delete() {
        val current = selectedArtist
        if (current != null) {
            AlertDialog.Builder(requireContext())
                .setTitle("Удалить художника")
                .setMessage("Вы действительно хотите удалить художника \"${current.name}\"?")
                .setPositiveButton("Да") { _, _ ->
                    lifecycleScope.launch {
                        viewModel.deleteArtist(current)
                        selectedArtist = null
                    }
                }
                .setNegativeButton("Отмена", null)
                .show()
        } else {
            AlertDialog.Builder(requireContext())
                .setMessage("Сначала выберите художника для удаления")
                .setPositiveButton("Ок", null)
                .show()
        }
    }
}