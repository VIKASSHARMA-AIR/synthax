package com.furyaxl.synthax.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.furyaxl.synthax.core.database.entity.TrackEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMediaRepository @Inject constructor(
    @ApplicationContext private val ctx: Context
) {
    suspend fun scan(): List<TrackEntity> = withContext(Dispatchers.IO) {
        val out = mutableListOf<TrackEntity>()
        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val proj = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )

        try {
            ctx.contentResolver.query(
                collection,
                proj,
                "${MediaStore.Audio.Media.IS_MUSIC} != 0",
                null,
                "${MediaStore.Audio.Media.TITLE} ASC"
            )?.use { c ->
                val iId = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val iT = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val iA = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val iAl = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val iAlId = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val iD = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

                while (c.moveToNext()) {
                    val id = c.getLong(iId)
                    val uri = ContentUris.withAppendedId(collection, id).toString()
                    val art = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"),
                        c.getLong(iAlId)
                    ).toString()

                    out += TrackEntity(
                        id = "local_$id",
                        title = c.getString(iT) ?: "Unknown Title",
                        artist = c.getString(iA) ?: "Unknown Artist",
                        album = c.getString(iAl),
                        artworkUrl = art,
                        streamUrl = uri,
                        durationMs = c.getLong(iD),
                        isLocal = true
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        out
    }
}

