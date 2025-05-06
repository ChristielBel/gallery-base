package com.example.gallery_base.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gallery_base.MainActivity
import com.example.gallery_base.NamesOfFragment
import com.example.gallery_base.R
import com.example.gallery_base.adapter.ExhibitionAdapter
import com.example.gallery_base.data.AppContainer
import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.databinding.FragmentExhibitionBinding
import kotlinx.coroutines.flow.onEach
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

    private var selectedExhibition: Exhibition? = null

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

        adapter = ExhibitionAdapter(
            onItemClick = { exhibition ->
                selectedExhibition = exhibition
                adapter.setSelectedId(exhibition.id)
            },
            onItemLongClick = { exhibition ->
                selectedExhibition = exhibition
                adapter.setSelectedId(exhibition.id)
                (activity as? MainActivity)?.showFragment(
                    NamesOfFragment.ARTIST,
                    null,
                    exhibition.id
                )
                Log.e("exxzx", "$exhibition")
                Log.e("exxzx", "${exhibition.id}")
            }
        )

        binding.rvExhibition.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExhibition.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exhibitions.collect { exhibitions ->
                Log.d("ExhibitionFragment", "Exhibitions updated: $exhibitions")
                adapter.submitList(exhibitions)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorMessage.collect { message ->
                message?.let {
                    showError(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showError(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Ошибка")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    override fun append() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_string, null)
        val editText = dialogView.findViewById<EditText>(R.id.etString)
        val textView = dialogView.findViewById<TextView>(R.id.tvInfo)
        textView.text = "Введите название новой выставки"

        AlertDialog.Builder(requireContext())
            .setTitle("Добавить выставку")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val input = editText.text.toString()
                if (input.isNotBlank()) {
                    lifecycleScope.launch {
                        try {
                            viewModel.insertExhibition(Exhibition(title = input))
                        } catch (e: Exception) {
                            showError(e.message ?: "Неизвестная ошибка")
                        }
                    }
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun update() {
        val current = selectedExhibition
        if (current != null) {
            val dialogView = layoutInflater.inflate(R.layout.dialog_string, null)
            val editText = dialogView.findViewById<EditText>(R.id.etString)
            val textView = dialogView.findViewById<TextView>(R.id.tvInfo)
            textView.text = "Измените название выставки"
            editText.setText(current.title)

            AlertDialog.Builder(requireContext())
                .setTitle("Обновить выставку")
                .setView(dialogView)
                .setPositiveButton("Сохранить") { _, _ ->
                    val updatedTitle = editText.text.toString()
                    if (updatedTitle.isNotBlank()) {
                        lifecycleScope.launch {
                            try {
                                viewModel.updateExhibition(current.copy(title = updatedTitle))
                            } catch (e: Exception) {
                                showError(e.message ?: "Неизвестная ошибка")
                            }
                        }
                    }
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    override fun delete() {
        val current = selectedExhibition
        if (current != null) {
            AlertDialog.Builder(requireContext())
                .setTitle("Удалить выставку")
                .setMessage("Вы действительно хотите удалить выставку \"${current.title}\"?")
                .setPositiveButton("Да") { _, _ ->
                    lifecycleScope.launch {
                        try {
                            viewModel.deleteExhibition(current)
                            selectedExhibition = null
                        } catch (e: Exception) {
                            showError(e.message ?: "Неизвестная ошибка")
                        }
                    }
                }
                .setNegativeButton("Отмена", null)
                .show()
        } else {
            AlertDialog.Builder(requireContext())
                .setMessage("Сначала выберите выставку для удаления")
                .setPositiveButton("Ок", null)
                .show()
        }
    }
}