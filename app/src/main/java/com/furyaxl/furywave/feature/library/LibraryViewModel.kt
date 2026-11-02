package com.furyaxl.furywave.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furyaxl.furywave.core.model.Playlist
import com.furyaxl.furywave.core.model.Track
import com.furyaxl.furywave.data.repository.PlaylistRepository
import com.furyaxl.furywave.data.repository.TrackRepository
import com.furyaxl.furywave.player.PlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val trackRepository: TrackRepository,
    private val playlistRepository: PlaylistRepository,
    private val playerController: PlayerController
) : ViewModel() {

    val playlists: StateFlow<List<Playlist>> = playlistRepository.observePlaylists()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val likedTracks: StateFlow<List<Track>> = trackRepository.observeLikedTracks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTracks: StateFlow<List<Track>> = trackRepository.observeAllTracks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            if (name.isNotBlank()) {
                playlistRepository.createPlaylist(name.trim())
            }
        }
    }

    fun playTrack(track: Track, tracks: List<Track>) {
        val index = tracks.indexOfFirst { it.id == track.id }.coerceAtLeast(0)
        playerController.playQueue(tracks, index)
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(playlistId)
        }
    }
}

