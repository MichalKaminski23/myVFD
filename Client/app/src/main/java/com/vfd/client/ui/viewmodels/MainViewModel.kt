package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.repositories.FirefighterRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firefighterRepository: FirefighterRepository,
) : ViewModel() {

    private val _pendingFirefighters = MutableStateFlow(0)
    val pendingFirefighters: StateFlow<Int> = _pendingFirefighters

    init {
        refreshBadges()
    }

    fun refreshBadges() {
        viewModelScope.launch {
            when (val result = firefighterRepository.getPendingFirefighters()) {

                is ApiResult.Success -> {
                    _pendingFirefighters.value = result.data?.size ?: 0
                }

                else -> {
                    _pendingFirefighters.value = 0
                }
            }
        }
    }
}
