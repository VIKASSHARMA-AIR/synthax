package com.furyaxl.furywave.feature.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import coil.compose.AsyncImage
import com.furyaxl.furywave.player.PlayerController
import com.furyaxl.furywave.ui.theme.DarkBackground
import com.furyaxl.furywave.ui.theme.DarkSurface
import com.furyaxl.furywave.ui.theme.ElectricCyan
import com.furyaxl.furywave.ui.theme.HotMagenta
import com.furyaxl.furywave.ui.theme.NeonViolet
import com.furyaxl.furywave.ui.theme.ProgressTrack
import com.furyaxl.furywave.ui.theme.TextMuted
import com.furyaxl.furywave.ui.theme.TextPrimary
import com.furyaxl.furywave.ui.theme.TextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    controller: PlayerController,
    onCollapse: () -> Unit,
    onNavigateToEqualizer: () -> Unit = {}
) {
    val state by controller.state.collectAsState()

    var isDraggingSlider by remember { mutableStateOf(false) }
    var dragPositionMs by remember { mutableFloatStateOf(0f) }

    val currentPositionMs = if (isDraggingSlider) {
        dragPositionMs.toLong()
    } else {
        state.positionMs
    }

    val durationMs = state.durationMs.coerceAtLeast(1L)
    val sliderValue = (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 24.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "NOW PLAYING",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                IconButton(onClick = onCollapse) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Collapse Player",
                        tint = TextPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = onNavigateToEqualizer) {
                    Icon(
                        imageVector = Icons.Default.Equalizer,
                        contentDescription = "Equalizer",
                        tint = ElectricCyan
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Center Album Artwork
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(20.dp), spotColor = NeonViolet)
                .clip(RoundedCornerShape(20.dp))
                .background(DarkSurface),
            contentAlignment = Alignment.Center
        ) {
            if (state.artworkUri != null) {
                AsyncImage(
                    model = state.artworkUri,
                    contentDescription = state.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkSurface
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = NeonViolet.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(72.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title & Artist & Like
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (state.title.isNotBlank()) state.title else "FuryWave Music",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (state.artist.isNotBlank()) state.artist else "Unknown Artist",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = { /* Like toggled via repo in ViewModel */ }) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Like Song",
                    tint = HotMagenta,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Scrubber Slider
        Slider(
            value = sliderValue,
            onValueChange = { fraction ->
                isDraggingSlider = true
                dragPositionMs = fraction * durationMs
            },
            onValueChangeFinished = {
                controller.seekTo(dragPositionMs.toLong())
                isDraggingSlider = false
            },
            colors = SliderDefaults.colors(
                thumbColor = ElectricCyan,
                activeTrackColor = ElectricCyan,
                inactiveTrackColor = ProgressTrack
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Time labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentPositionMs),
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
            Text(
                text = formatTime(state.durationMs),
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Primary Playback Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shuffle
            IconButton(onClick = { controller.setShuffle(!state.shuffle) }) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Shuffle",
                    tint = if (state.shuffle) ElectricCyan else TextMuted
                )
            }

            // Previous
            IconButton(onClick = { controller.previous() }) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Previous Track",
                    tint = TextPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Play / Pause FAB
            FloatingActionButton(
                onClick = { controller.togglePlay() },
                containerColor = ElectricCyan,
                contentColor = DarkBackground,
                shape = CircleShape,
                modifier = Modifier.size(68.dp)
            ) {
                Icon(
                    imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (state.isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(36.dp)
                )
            }

            // Next
            IconButton(onClick = { controller.next() }) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next Track",
                    tint = TextPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Repeat
            IconButton(onClick = { controller.cycleRepeat() }) {
                val icon = when (state.repeatMode) {
                    Player.REPEAT_MODE_ONE -> Icons.Default.RepeatOne
                    else -> Icons.Default.Repeat
                }
                val tint = if (state.repeatMode != Player.REPEAT_MODE_OFF) ElectricCyan else TextMuted
                Icon(
                    imageVector = icon,
                    contentDescription = "Repeat",
                    tint = tint
                )
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}

