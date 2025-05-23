package com.example.gallery_base.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.gallery_base.MainActivity
import com.example.gallery_base.MyApplication
import com.example.gallery_base.R
import com.example.gallery_base.adapter.ArtistAdapter
import com.example.gallery_base.data.Artist
import com.example.gallery_base.databinding.FragmentArtistBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import java.util.UUID

class ArtistFragment : Fragment(), MainActivity.Edit {

    companion object {
        private const val ARG_EXHIBITION_ID = "exhibition_id"

        fun newInstance(exhibitionId: UUID): ArtistFragment {
            return ArtistFragment().apply {
                arguments = Bundle().apply {
                    Log.d("Artist", "$exhibitionId")
                    putUUID(ARG_EXHIBITION_ID, exhibitionId)
                }
            }
        }
    }

    private lateinit var binding: FragmentArtistBinding
    private val viewModel: ArtistViewModel by viewModels {
        ArtistViewModelFactory((requireActivity().application as MyApplication).appContainer.artistRepository)
    }
    private var selectedArtist: Artist? = null
    private lateinit var exhibitionId: UUID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        FragmentArtistBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedArtist?.let {
            outState.putString("SELECTED_ARTIST_ID", it.id.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exhibitionId = requireArguments().getUUID(ARG_EXHIBITION_ID)
            ?: throw IllegalStateException("Exhibition ID must not be null")

        Log.d("ArtistFragment", "onViewCreated, exhibitionId: $exhibitionId")

        binding.vpArtists.offscreenPageLimit = 3

        viewModel.getArtistsByExhibition(exhibitionId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.artistsByExhibition.collect { artists ->
                    setupViewPagerWithTabs(artists)
                }
            }
        }
    }

    private fun setupViewPagerWithTabs(artists: List<Artist>) {
        if (artists.isEmpty()) {
            return
        }

        val pagerAdapter = ArtistAdapter(
            fragment = this,
            artists = artists,
            onArtistSelected = { artist ->
                selectedArtist = artist
            }
        )

        binding.vpArtists.adapter = pagerAdapter
        TabLayoutMediator(binding.tlArtists, binding.vpArtists) { tab, position ->
            tab.text = artists[position].name
        }.attach()
    }

    private fun updateTabSelection(position: Int) {
        val tab = binding.tlArtists.getTabAt(position)
        tab?.select()
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
                Log.e("append", "$exhibitionId")
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

    fun Bundle.putUUID(key: String, value: UUID) {
        putString(key, value.toString())
    }

    fun Bundle.getUUID(key: String): UUID? {
        return getString(key)?.let { UUID.fromString(it) }
    }
}