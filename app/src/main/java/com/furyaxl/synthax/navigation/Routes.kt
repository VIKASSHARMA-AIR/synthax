package com.furyaxl.synthax.navigation

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object Search : Route("search")
    data object Library : Route("library")
    data object Settings : Route("settings")
    data object NowPlaying : Route("now_playing")
    data class PlaylistDetail(val playlistId: Long) : Route("playlist/{id}") {
        companion object {
            const val ROUTE_DEF = "playlist/{id}"
            fun createRoute(playlistId: Long) = "playlist/$playlistId"
        }
    }
}

