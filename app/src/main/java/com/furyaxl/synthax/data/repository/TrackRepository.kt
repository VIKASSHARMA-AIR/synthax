package com.furyaxl.synthax.data.repository

import com.furyaxl.synthax.core.database.dao.TrackDao
import com.furyaxl.synthax.core.database.entity.LikedTrackEntity
import com.furyaxl.synthax.core.model.Track
import com.furyaxl.synthax.data.mapper.toEntity
import com.furyaxl.synthax.data.mapper.toTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackRepository @Inject constructor(
    private val trackDao: TrackDao,
    private val localMediaRepository: LocalMediaRepository
) {
    fun observeAllTracks(): Flow<List<Track>> =
        trackDao.observeAll().map { entities -> entities.map { it.toTrack() } }

    fun observeLikedTracks(): Flow<List<Track>> =
        trackDao.observeLiked().map { entities -> entities.map { it.toTrack(isLiked = true) } }

    fun searchTracks(query: String): Flow<List<Track>> =
        trackDao.search(query).map { entities -> entities.map { it.toTrack() } }

    fun isTrackLiked(trackId: String): Flow<Boolean> =
        trackDao.isLiked(trackId)

    suspend fun syncLocalTracks(): Int {
        val scanned = localMediaRepository.scan()
        if (scanned.isNotEmpty()) {
            trackDao.upsertAll(scanned)
        }
        return scanned.size
    }

    suspend fun saveTracks(tracks: List<Track>) {
        trackDao.upsertAll(tracks.map { it.toEntity() })
    }

    suspend fun toggleLike(trackId: String, currentLiked: Boolean) {
        if (currentLiked) {
            trackDao.unlike(trackId)
        } else {
            trackDao.like(LikedTrackEntity(trackId = trackId))
        }
    }
}

