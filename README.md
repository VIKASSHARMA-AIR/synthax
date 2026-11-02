# FuryWave

An open-source Android music player by **FURYAXL**.

- Kotlin, Jetpack Compose, Media3, Room, Hilt
- Plays local music; streaming support via your own or licensed catalogs
- Package: `com.furyaxl.furywave`
- License: MIT

## Build
```bash
./gradlew assembleDebug
```

## Architecture & Tech Stack
- **Architecture**: Single-Activity, Clean Architecture (UI -> ViewModel -> Repository -> Room / MediaStore)
- **UI**: Jetpack Compose & Material 3, Dark & Modern Theme
- **Playback**: AndroidX Media3 (ExoPlayer + MediaSessionService) with background playback and notification support
- **Database**: Room local database for tracks, playlists, and liked songs
- **Dependency Injection**: Dagger Hilt
- **Asynchronous**: Kotlin Coroutines & StateFlow

## Music content
FuryWave ships with no music. Use files you own or catalogs whose license allows it
(for example Creative Commons sources, with attribution).

## Contributing
Pull requests are welcome. Please do not submit code or assets copied from other apps.

