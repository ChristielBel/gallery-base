package com.example.gallery_base.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gallery_base.data.Artist
import com.example.gallery_base.repository.ArtistRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ArtistViewModel(private val repository: ArtistRepository) : ViewModel() {
    val artists: StateFlow<List<Artist>> =
        repository.getAllArtists()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    // Метод для добавления художника
    fun insertArtist(artist: Artist) {
        viewModelScope.launch {
            repository.insertArtist(artist)
        }
    }

    // Метод для обновления художника
    fun updateArtist(artist: Artist) {
        viewModelScope.launch {
            repository.updateArtist(artist)
        }
    }

    // Метод для удаления художника
    fun deleteArtist(artist: Artist) {
        viewModelScope.launch {
            repository.deleteArtist(artist)
        }
    }

    fun getArtistsByExhibition(exhibitionId: UUID): StateFlow<List<Artist>> {
        return repository.getByExhibition(exhibitionId)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }
}

class ArtistViewModelFactory(
    private val repository: ArtistRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArtistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}