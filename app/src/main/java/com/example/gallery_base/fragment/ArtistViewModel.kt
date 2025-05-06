package com.example.gallery_base.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gallery_base.data.Artist
import com.example.gallery_base.repository.ArtistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ArtistViewModel(private val repository: ArtistRepository) : ViewModel() {
    private val _artistsByExhibition = MutableStateFlow<List<Artist>>(emptyList())
    val artistsByExhibition: StateFlow<List<Artist>> = _artistsByExhibition

    init {
        Log.d("ArtistViewModel", "ViewModel initialized")
    }

    fun insertArtist(artist: Artist) {
        viewModelScope.launch {
            repository.insertArtist(artist)
            getArtistsByExhibition(artist.exhibitionId!!)
        }
    }

    fun updateArtist(artist: Artist) {
        viewModelScope.launch {
            repository.updateArtist(artist)
        }
    }

    fun deleteArtist(artist: Artist) {
        viewModelScope.launch {
            repository.deleteArtist(artist)
        }
    }

    fun getArtistsByExhibition(exhibitionId: UUID) {
        Log.d("ArtistViewModel", "Getting artists for exhibition: $exhibitionId")
        viewModelScope.launch {
            repository.getByExhibition(exhibitionId).collect {
                _artistsByExhibition.value = it
            }
        }
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