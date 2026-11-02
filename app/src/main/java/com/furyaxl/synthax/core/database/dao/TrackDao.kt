package com.furyaxl.synthax.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.furyaxl.synthax.core.database.entity.LikedTrackEntity
import com.furyaxl.synthax.core.database.entity.PlaylistEntity
import com.furyaxl.synthax.core.database.entity.PlaylistTrackCrossRef
import com.furyaxl.synthax.core.database.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks ORDER BY title ASC")
    fun observeAll(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE title LIKE '%' || :q || '%' OR artist LIKE '%' || :q || '%'")
    fun search(q: String): Flow<List<TrackEntity>>

    @Query("SELECT t.* FROM tracks t JOIN liked_tracks l ON t.id = l.trackId ORDER BY l.likedAt DESC")
    fun observeLiked(): Flow<List<TrackEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM liked_tracks WHERE trackId = :id)")
    fun isLiked(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<TrackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun like(item: LikedTrackEntity)

    @Query("DELETE FROM liked_tracks WHERE trackId = :id")
    suspend fun unlike(id: String)
}

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    fun observePlaylists(): Flow<List<PlaylistEntity>>

    @Query("""
        SELECT t.* FROM tracks t 
        JOIN playlist_tracks pt ON t.id = pt.trackId 
        WHERE pt.playlistId = :playlistId 
        ORDER BY pt.position ASC
    """)
    fun observeTracksForPlaylist(playlistId: Long): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlaylist(crossRef: PlaylistTrackCrossRef)

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylist(id: Long)

    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: String)
}

