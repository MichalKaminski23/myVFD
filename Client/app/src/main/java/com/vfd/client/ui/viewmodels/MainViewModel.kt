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

    private val _activeFirefighters = MutableStateFlow(0)
    val activeFirefighters: StateFlow<Int> = _activeFirefighters

    init {
        refreshBadges()
    }

    fun refreshBadges() {
        viewModelScope.launch {

            val pending =
                when (val result =
                    firefighterRepository.getPendingFirefighters(page = 0, size = 1)) {
                    is ApiResult.Success -> result.data?.totalElements ?: result.data?.items?.size
                    ?: 0

                    else -> 0
                }
            _pendingFirefighters.value = pending

            val active =
                when (val result = firefighterRepository.getFirefighters(page = 0, size = 1)) {
                    is ApiResult.Success -> result.data?.totalElements ?: result.data?.items?.size
                    ?: 0

                    else -> 0
                }
            _activeFirefighters.value = active
        }
    }
}