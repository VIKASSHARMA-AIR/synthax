package com.furyaxl.furywave.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.furyaxl.furywave.core.database.dao.PlaylistDao
import com.furyaxl.furywave.core.database.dao.TrackDao
import com.furyaxl.furywave.core.database.entity.LikedTrackEntity
import com.furyaxl.furywave.core.database.entity.PlaylistEntity
import com.furyaxl.furywave.core.database.entity.PlaylistTrackCrossRef
import com.furyaxl.furywave.core.database.entity.TrackEntity

@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackCrossRef::class,
        LikedTrackEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}

