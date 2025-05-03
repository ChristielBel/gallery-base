package com.example.gallery_base.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.repository.ExhibitionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExhibitionViewModel(private val repository: ExhibitionRepository) : ViewModel() {
    private val _exhibitions = MutableStateFlow<List<Exhibition>>(emptyList())
    val exhibitions: StateFlow<List<Exhibition>> = _exhibitions

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadExhibitions()
    }

    private fun loadExhibitions() {
        viewModelScope.launch {
            repository.getAllExhibitions()
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .onEach { list ->
                    _exhibitions.value = list
                    Log.d("ExhibitionViewModel", "Exhibitions updated: $list") // Добавьте логирование
                }
                .collect { list ->
                    _exhibitions.value = list
                }
        }
    }

    fun insertExhibition(exhibition: Exhibition) {
        viewModelScope.launch {
            try {
                repository.insertExhibition(exhibition)
                loadExhibitions()
                Log.d("ExhibitionViewModel", "Exhibition inserted successfully")
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка добавления: ${e.message}"
            }
        }
    }

    fun updateExhibition(exhibition: Exhibition) {
        viewModelScope.launch {
            try {
                repository.updateExhibition(exhibition)
                Log.d("ExhibitionViewModel", "Exhibition inserted successfully")
                loadExhibitions()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка обновления: ${e.message}"
            }
        }
    }

    fun deleteExhibition(exhibition: Exhibition) {
        viewModelScope.launch {
            try {
                repository.deleteExhibition(exhibition)
                loadExhibitions()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка удаления: ${e.message}"
            }
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