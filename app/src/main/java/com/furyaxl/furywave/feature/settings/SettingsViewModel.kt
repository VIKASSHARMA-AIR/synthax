package com.furyaxl.furywave.feature.settings

import androidx.lifecycle.ViewModel
import com.furyaxl.furywave.player.EqPreset
import com.furyaxl.furywave.player.EqualizerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val equalizerManager: EqualizerManager
) : ViewModel() {

    private val _selectedPreset = MutableStateFlow(equalizerManager.currentPreset)
    val selectedPreset: StateFlow<EqPreset> = _selectedPreset.asStateFlow()

    fun selectPreset(preset: EqPreset) {
        equalizerManager.init()
        equalizerManager.applyPreset(preset)
        _selectedPreset.value = preset
    }
}

