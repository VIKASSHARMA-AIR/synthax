package com.furyaxl.synthax.player

import android.media.audiofx.Equalizer
import androidx.media3.exoplayer.ExoPlayer
import javax.inject.Inject
import javax.inject.Singleton

enum class EqPreset(val displayName: String) {
    FLAT("Flat"),
    BASS_BOOST("Bass Boost"),
    POP("Pop"),
    ROCK("Rock"),
    HIP_HOP("Hip-Hop"),
    ELECTRONIC("Electronic"),
    VOCAL("Vocal")
}

@Singleton
class EqualizerManager @Inject constructor(
    private val exoPlayer: ExoPlayer
) {
    private var equalizer: Equalizer? = null
    var currentPreset: EqPreset = EqPreset.FLAT
        private set

    fun init() {
        try {
            val audioSessionId = exoPlayer.audioSessionId
            if (audioSessionId != 0 && equalizer == null) {
                equalizer = Equalizer(0, audioSessionId).apply {
                    enabled = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun applyPreset(preset: EqPreset) {
        currentPreset = preset
        val eq = equalizer ?: return
        val numBands = eq.numberOfBands.toInt()
        if (numBands <= 0) return

        val bandRange = eq.bandLevelRange
        val minLevel = bandRange[0]
        val maxLevel = bandRange[1]
        val midLevel = (maxLevel + minLevel) / 2

        when (preset) {
            EqPreset.FLAT -> {
                for (i in 0 until numBands) {
                    eq.setBandLevel(i.toShort(), 0)
                }
            }
            EqPreset.BASS_BOOST -> {
                for (i in 0 until numBands) {
                    val level = if (i < numBands / 2) (maxLevel * 0.7f).toInt().toShort() else 0
                    eq.setBandLevel(i.toShort(), level)
                }
            }
            EqPreset.POP -> {
                for (i in 0 until numBands) {
                    val level = if (i == 0 || i == numBands - 1) (maxLevel * 0.4f).toInt().toShort() else (minLevel * 0.2f).toInt().toShort()
                    eq.setBandLevel(i.toShort(), level)
                }
            }
            EqPreset.ROCK -> {
                for (i in 0 until numBands) {
                    val level = if (i < 2 || i >= numBands - 2) (maxLevel * 0.5f).toInt().toShort() else (minLevel * 0.3f).toInt().toShort()
                    eq.setBandLevel(i.toShort(), level)
                }
            }
            EqPreset.HIP_HOP -> {
                for (i in 0 until numBands) {
                    val level = if (i == 0 || i == 1) (maxLevel * 0.8f).toInt().toShort() else (maxLevel * 0.2f).toInt().toShort()
                    eq.setBandLevel(i.toShort(), level)
                }
            }
            EqPreset.ELECTRONIC -> {
                for (i in 0 until numBands) {
                    val level = if (i == 0 || i == numBands - 1) (maxLevel * 0.6f).toInt().toShort() else 0
                    eq.setBandLevel(i.toShort(), level)
                }
            }
            EqPreset.VOCAL -> {
                for (i in 0 until numBands) {
                    val level = if (i in 1 until numBands - 1) (maxLevel * 0.5f).toInt().toShort() else (minLevel * 0.3f).toInt().toShort()
                    eq.setBandLevel(i.toShort(), level)
                }
            }
        }
    }

    fun release() {
        equalizer?.release()
        equalizer = null
    }
}

