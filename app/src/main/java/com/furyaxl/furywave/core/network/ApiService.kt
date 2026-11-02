package com.furyaxl.furywave.core.network

import kotlinx.serialization.Serializable
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
data class TrackDto(
    val id: String,
    val title: String,
    val artist: String,
    val album: String? = null,
    val artworkUrl: String? = null,
    val streamUrl: String,
    val durationMs: Long = 0,
    val lyricsLrc: String? = null
)

@Serializable
data class HomeResponse(
    val featured: List<TrackDto> = emptyList(),
    val trending: List<TrackDto> = emptyList(),
    val recommended: List<TrackDto> = emptyList()
)

@Serializable
data class SearchResponse(
    val tracks: List<TrackDto> = emptyList()
)

interface ApiService {
    @GET("v1/home")
    suspend fun home(): HomeResponse

    @GET("v1/search")
    suspend fun search(@Query("q") q: String): SearchResponse

    @GET("v1/tracks/{id}")
    suspend fun track(@Path("id") id: String): TrackDto

    @GET("v1/me/likes")
    suspend fun likes(): List<TrackDto>

    @PUT("v1/me/likes/{id}")
    suspend fun like(@Path("id") id: String)

    @DELETE("v1/me/likes/{id}")
    suspend fun unlike(@Path("id") id: String)
}

