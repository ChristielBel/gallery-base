package com.example.gallery_base.fragment

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gallery_base.MainActivity
import com.example.gallery_base.R
import com.example.gallery_base.adapter.ExhibitionAdapter
import com.example.gallery_base.data.AppContainer
import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.databinding.FragmentExhibitionBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExhibitionFragment : Fragment(), MainActivity.Edit {

    private var _binding: FragmentExhibitionBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ExhibitionAdapter
    private val viewModel: ExhibitionViewModel by viewModels {
        ExhibitionViewModelFactory(
            AppContainer(requireContext()).exhibitionRepository
        )
    }


    companion object {
        fun newInstance() = ExhibitionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExhibitionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ExhibitionAdapter()
        binding.rvExhibition.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExhibition.adapter = adapter

        // Подписка на Flow из ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exhibitions.collectLatest { exhibitions ->
                adapter.submitList(exhibitions)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun append() {
        AlertDialog.Builder(requireContext())
            .setTitle("Добавить выставку")
            .setMessage("Вы действительно хотите добавить новую выставку?")
            .setPositiveButton("Да") { _, _ ->
                val newExhibition = Exhibition(title = "Новая выставка")
                lifecycleScope.launch {
                    viewModel.insertExhibition(newExhibition)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun update() {
        val current = viewModel.exhibitions.value.firstOrNull()
        if (current != null) {
            AlertDialog.Builder(requireContext())
                .setTitle("Обновить выставку")
                .setMessage("Вы действительно хотите обновить первую выставку?")
                .setPositiveButton("Да") { _, _ ->
                    val updated = current.copy(title = current.title + " (Обновлено)")
                    lifecycleScope.launch {
                        viewModel.updateExhibition(updated)
                    }
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    override fun delete() {
        val current = viewModel.exhibitions.value.firstOrNull()
        if (current != null) {
            AlertDialog.Builder(requireContext())
                .setTitle("Удалить выставку")
                .setMessage("Вы действительно хотите удалить первую выставку?")
                .setPositiveButton("Да") { _, _ ->
                    lifecycleScope.launch {
                        viewModel.deleteExhibition(current)
                    }
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

}