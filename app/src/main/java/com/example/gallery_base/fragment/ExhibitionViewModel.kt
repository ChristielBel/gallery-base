package com.example.gallery_base.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.repository.ExhibitionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExhibitionViewModel(private val repository: ExhibitionRepository) : ViewModel() {
    val exhibitions: StateFlow<List<Exhibition>> = repository.getAllExhibitions()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insertExhibition(exhibition: Exhibition) {
        viewModelScope.launch {
            repository.insertExhibition(exhibition)
        }
    }

    fun updateExhibition(exhibition: Exhibition) {
        viewModelScope.launch {
            repository.updateExhibition(exhibition)
        }
    }

    fun deleteExhibition(exhibition: Exhibition) {
        viewModelScope.launch {
            repository.deleteExhibition(exhibition)
        }
    }
}

class ExhibitionViewModelFactory(
    private val repository: ExhibitionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExhibitionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExhibitionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}