package com.example.gallery_base.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gallery_base.data.Painting
import com.example.gallery_base.repository.PaintingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class PaintingViewModel(private val repository: PaintingRepository) : ViewModel() {

    fun getPaintingsByArtist(artistId: UUID): StateFlow<List<Painting>> {
        return repository.getByArtist(artistId)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun insertPainting(painting: Painting) {
        viewModelScope.launch {
            repository.insertPainting(painting)
        }
    }

    fun updatePainting(painting: Painting) {
        viewModelScope.launch {
            repository.updatePainting(painting)
        }
    }

    fun deletePainting(painting: Painting) {
        viewModelScope.launch {
            repository.deletePainting(painting)
        }
    }
}

class PaintingViewModelFactory(
    private val repository: PaintingRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaintingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaintingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}