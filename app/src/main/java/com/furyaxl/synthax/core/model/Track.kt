package com.furyaxl.synthax.core.model

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val album: String? = null,
    val artworkUrl: String? = null,
    val streamUrl: String,
    val durationMs: Long = 0,
    val isLocal: Boolean = false,
    val localPath: String? = null,
    val lyricsLrc: String? = null,
    val isLiked: Boolean = false
)

data class Playlist(
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val trackCount: Int = 0
)

data class Artist(
    val id: String,
    val name: String,
    val artworkUrl: String? = null
)

