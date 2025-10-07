package com.vfd.client.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.repositories.AssetRepository
import com.vfd.client.data.repositories.EventRepository
import com.vfd.client.data.repositories.FirefighterRepository
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.daysUntilSomething
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firefighterRepository: FirefighterRepository,
    private val assetRepository: AssetRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _pendingFirefighters = MutableStateFlow(0)
    val pendingFirefighters: StateFlow<Int> = _pendingFirefighters

    private val _activeFirefighters = MutableStateFlow(0)
    val activeFirefighters: StateFlow<Int> = _activeFirefighters

    val _totalAssets = MutableStateFlow(0)
    val totalAssets: StateFlow<Int> = _totalAssets

    val _upcomingEvents = MutableStateFlow(0)
    val upcomingEvents: StateFlow<Int> = _upcomingEvents

    private val _canCreateThings = MutableStateFlow(false)
    val canCreateThings: StateFlow<Boolean> = _canCreateThings.asStateFlow()

    private val _hasRole = MutableStateFlow(false)
    val hasRole: StateFlow<Boolean> = _hasRole.asStateFlow()

    fun setUserRole(role: String?) {
        _canCreateThings.value = role?.uppercase() == "PRESIDENT"
        _hasRole.value = !role.isNullOrBlank()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                when (val result =
                    firefighterRepository.getFirefighters(page = 0, size = 1)) {
                    is ApiResult.Success -> result.data?.totalElements ?: result.data?.items?.size
                    ?: 0

                    else -> 0
                }
            _activeFirefighters.value = active

            val assets =
                when (val result = assetRepository.getAssets(page = 0, size = 1)) {
                    is ApiResult.Success -> result.data?.totalElements ?: result.data?.items?.size
                    ?: 0

                    else -> 0
                }
            _totalAssets.value = assets

            val eventsCount =
                when (val result = eventRepository.getEvents(page = 0, size = 20)) {
                    is ApiResult.Success -> {
                        result.data?.items?.count { dto ->
                            val daysLeft = daysUntilSomething(dto.eventDate)
                            daysLeft in 0..30
                        } ?: 0
                    }

                    else -> 0
                }
            _upcomingEvents.value = eventsCount
        }
    }
}