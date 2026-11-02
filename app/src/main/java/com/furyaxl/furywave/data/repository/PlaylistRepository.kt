package com.furyaxl.furywave.data.repository

import com.furyaxl.furywave.core.database.dao.PlaylistDao
import com.furyaxl.furywave.core.database.entity.PlaylistEntity
import com.furyaxl.furywave.core.database.entity.PlaylistTrackCrossRef
import com.furyaxl.furywave.core.model.Playlist
import com.furyaxl.furywave.core.model.Track
import com.furyaxl.furywave.data.mapper.toTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepository @Inject constructor(
    private val playlistDao: PlaylistDao
) {
    fun observePlaylists(): Flow<List<Playlist>> =
        playlistDao.observePlaylists().map { entities ->
            entities.map {
                Playlist(
                    id = it.id,
                    name = it.name,
                    createdAt = it.createdAt
                )
            }
        }

    fun observeTracksForPlaylist(playlistId: Long): Flow<List<Track>> =
        playlistDao.observeTracksForPlaylist(playlistId).map { entities ->
            entities.map { it.toTrack() }
        }

    suspend fun createPlaylist(name: String): Long =
        playlistDao.insertPlaylist(PlaylistEntity(name = name))

    suspend fun addTrackToPlaylist(playlistId: Long, trackId: String, position: Int = 0) {
        playlistDao.addTrackToPlaylist(PlaylistTrackCrossRef(playlistId, trackId, position))
    }

    suspend fun deletePlaylist(playlistId: Long) {
        playlistDao.deletePlaylist(playlistId)
    }

    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: String) {
        playlistDao.removeTrackFromPlaylist(playlistId, trackId)
    }
}

