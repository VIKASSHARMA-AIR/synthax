package com.furyaxl.synthax.data.mapper

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.furyaxl.synthax.core.database.entity.TrackEntity
import com.furyaxl.synthax.core.model.Track
import com.furyaxl.synthax.core.network.TrackDto

fun TrackEntity.toTrack(isLiked: Boolean = false): Track = Track(
    id = id,
    title = title,
    artist = artist,
    album = album,
    artworkUrl = artworkUrl,
    streamUrl = streamUrl,
    durationMs = durationMs,
    isLocal = isLocal,
    localPath = localPath,
    lyricsLrc = lyricsLrc,
    isLiked = isLiked
)

fun Track.toEntity(): TrackEntity = TrackEntity(
    id = id,
    title = title,
    artist = artist,
    album = album,
    artworkUrl = artworkUrl,
    streamUrl = streamUrl,
    durationMs = durationMs,
    isLocal = isLocal,
    localPath = localPath,
    lyricsLrc = lyricsLrc
)

fun TrackDto.toTrack(): Track = Track(
    id = id,
    title = title,
    artist = artist,
    album = album,
    artworkUrl = artworkUrl,
    streamUrl = streamUrl,
    durationMs = durationMs,
    isLocal = false,
    lyricsLrc = lyricsLrc
)

fun Track.toMediaItem(): MediaItem = MediaItem.Builder()
    .setMediaId(id)
    .setUri(localPath ?: streamUrl)
    .setMediaMetadata(
        MediaMetadata.Builder()
            .setTitle(title)
            .setArtist(artist)
            .setAlbumTitle(album)
            .setArtworkUri(artworkUrl?.let(Uri::parse))
            .build()
    )
    .build()

